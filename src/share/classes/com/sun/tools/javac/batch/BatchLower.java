package com.sun.tools.javac.batch;

import static com.sun.tools.javac.code.Flags.PUBLIC;
import batch.partition.CodeModel;
import batch.partition.DynamicCallInfo;
import batch.partition.Environment;
import batch.partition.History;
import batch.partition.PExpr;
import batch.partition.Place;
import batch.partition.Stage;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.comp.Lower;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

public class BatchLower extends Lower {
  Context context;
  boolean inBatch;

  public BatchLower(Context context) {
    super(context);
    this.context = context;
  }

  public void visitMethodDefInternal(JCMethodDecl decl) {

    Class<batch.IncludeInBatch> annoType = batch.IncludeInBatch.class;
    boolean batchMethod = (null != JavacElements.getAnnotation(decl.sym,
        annoType));
    if (!batchMethod) {
      super.visitMethodDefInternal(decl);
      return;
    }

    JavaToBatch trans = new JavaToBatch(types, syms);
    DynamicCallInfo info = trans.getMethodInfo(decl.sym);

    // <E> E <method$getRemote>(batch.util.ForestWriter s$, batch.Factory<E> f$, E <arg1>...) { 
    //    <pre-local(out=s$)>
    //    E e$ = <remote(factory=f$)>
    //    e$ = f$.Let("<param>", <param>, e$);
    //    ...
    //    return e$;
    //  }
    //   <E> <method$postLocal>(batch.util.ForestReader r$) { 
    //      <post-local(out=r$)>
    //   }

    CodeModel codeModel = new CodeModel();
    Environment env = new Environment(codeModel);

    VarSymbol send = new VarSymbol(0, names.fromString("s$"),
        syms.batchForestWriterType, decl.sym);
    VarSymbol receive = new VarSymbol(0, names.fromString("r$"),
        syms.batchForestReaderType, decl.sym);
    Name nE = names.fromString("E");
    TypeVar E = new TypeVar(nE, decl.sym, syms.objectType);
    E.bound = syms.objectType;
    VarSymbol e = new VarSymbol(0, names.fromString("e$"), E, decl.sym);

    // MAKE THE NEW PARAMETER LIST
    //     (batch.util.ForestWriter in, batch.Factory<E> f$, E <arg1>...)
    List<JCVariableDecl> newParams = List.nil();
    // batch.util.ForestWriter s$;
    newParams = newParams.append(make.VarDef(send, null));
    // batch.BatchFactory<E> f$,
    Type factType = new Type.ClassType(Type.noType, List.<Type> of(E),
        syms.batchFactoryType.tsym);
    VarSymbol fact = new VarSymbol(0, names.fromString("f$"), factType,
        currentMethodSym);
    JCVariableDecl fdef = make.VarDef(fact, null);
    newParams = newParams.append(fdef);
    // add the original arguments
    int i = 0;
    for (JCVariableDecl p : decl.getParameters()) {
      Place place = info.arguments.get(i++);
      env = env.extend(
        p.name.toString(),
        CodeModel.makeExtra(DynamicCallInfo.TYPE_INFO_KEY, p.type),
        place);
      if (place == Place.REMOTE) {
        VarSymbol varWithTypeE = new Symbol.VarSymbol(0, p.name, E, decl.sym); // TODO: owner should be method we haven't created yet
        newParams = newParams.append(make.VarDef(varWithTypeE, null));
      } else
        newParams = newParams.append((JCVariableDecl) p.clone());
    }

    // translate the body

    PExpr exp = decl.getBody().accept(trans,
        new BatchTransformInfo(info.returns == Place.REMOTE));

    History h = exp.partition(Place.MOBILE, env);
    System.out.println("----METHOD: " + decl.sym + " ------------------------");
    System.out.println(exp.toString());
    System.out.println(h.toString());

    JCGenerator gen = new JCGenerator(context, currentMethodSym, fact, E,
        attrEnv, null);
    gen.setInOut(null, send);

    // generate the pre-local
    List<JCStatement> stats = List.nil();
    int step = 0;
    if (h.get(step).place() == Place.LOCAL) {
      // local code using the forest
      Stage s1 = h.get(step);
      stats = stats.append(s1.action().runExtra(gen).generateStmt());
      step++;
    }

    // generate the remote script
    //    E e$ = <remote>
    Stage s2 = codeModel.emptyStage();
    if (step < h.length() && h.get(step).place() == Place.REMOTE)
      s2 = h.get(step++);

    JCExpression remote = s2.action().runExtra(gen).generateRemote();
    JCStatement cmd;
    cmd = make.VarDef(e, remote);
    cmd.setType(syms.batchForestReaderType);
    stats = stats.append(cmd);

    // make the Lets
    // add the original arguments
    i = 0;
    for (JCVariableDecl p : newParams.tail.tail) {
      Place place = info.arguments.get(i++);
      if (place == Place.REMOTE) {
        //    e$ = f$.Let("<param>", <param>, e$);
        JCExpression let = gen.call(make.Ident(fact), "Let", E,
            make.Literal(p.name.toString()), make.Ident(p.sym), make.Ident(e));
        stats = stats.append(make.Assignment(e, let));
      }
    }

    stats = stats.append(make.Return(make.Ident(e)));
    // TODO: have to bind the parameters with LETs
    // TODO: make sure there are not NAME CLASHES in the binding!!!!

    // <E> E <method>$getRemote(batch.util.ForestWriter in, batch.Factory<E> f$, E <arg1>...)

    //    Modifiers newMods = mods.fullCopy();
    //    if (!newMods.isStatic() && !hostType().isAnonymous())
    //      newMods.addModifier(new Modifier("static"));

    List<Type> argtypes = List.nil();
    for (JCVariableDecl arg : newParams) {
      argtypes = argtypes.append(arg.type);
    }

    Name getRemote = names.fromString(decl.name.toString() + "$getRemote");

    JCTypeParameter paramE = make.TypeParameter(nE,
        List.<JCExpression> of(make.Type(syms.objectType)));
    JCMethodDecl method1 = make.MethodDef(decl.mods, getRemote, make.Type(E),
        List.<JCTypeParameter> of(paramE), newParams,
        List.<JCExpression> nil(), make.Block(0, stats), null);

    Type getRemoteType = new Type.ForAll(List.<Type> of(E), new MethodType(
        argtypes, E, List.<Type> nil(), syms.methodClass));
    method1.sym = new MethodSymbol(decl.mods.flags, getRemote, getRemoteType,
        decl.sym.owner);

    // ADD the new method to the class definition
    ClassSymbol clz = (ClassSymbol) decl.sym.owner;
    JCClassDecl cld = classDef(clz);
    cld.defs = cld.defs.prepend(method1);
    cld.sym.members_field.enter(method1.sym);

    // create the post-local, if there is one
    //   <E> method$postLocal(batch.util.ForestReader receive) { 
    //      <post-local>
    //   }
    if (step < h.length() && h.get(step).place() == Place.LOCAL) {
      // has post local stage
      Stage s3 = h.get(step++);
      // <type> <method>$postLocal(batch.util.ForestReader out) { 
      //    <post-local>
      // }
      gen.setInOut(receive, null);
      stats = List.of(s3.action().runExtra(gen).generateStmt());
      newParams = List.of(make.VarDef(receive, null));

      Name postLocal = names.fromString(decl.name.toString() + "$postLocal");
      MethodType postLocalType = new MethodType(
          List.<Type> of(syms.batchForestReaderType), decl.sym.getReturnType(),
          List.<Type> nil(), syms.methodClass);

      MethodSymbol postLocalSym = new MethodSymbol(PUBLIC, postLocal,
          postLocalType, decl.sym.owner);
      JCMethodDecl method2 = make.MethodDef(postLocalSym, make.Block(0, stats));
      // (Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5)
      cld = classDef(clz);
      cld.defs = cld.defs.prepend(method2);
    }
    super.visitMethodDefInternal(decl);
  }

  public void visitForeachLoop(JCEnhancedForLoop tree) {
    Type batchType = types.asSuper(types.upperBound(tree.expr.type),
        syms.batchServiceType.tsym);
    if (batchType != null)
      visitBatchBlock(tree, batchType);
    else
      super.visitForeachLoop(tree);
  }

  // for (interface var : service) { block }
  //
  // translates to...
  //
  // batch.util.BatchExpressionFactory f$ = <exp>;
  // batch.util.Forest s$ = new batch.util.Forest();
  // <prelocal(out=s$)>
  // batch.util.ForestReader r$ = <service>.execute(<remote(factory=f$)>, s$);
  // <postlocal(in=r$)>
  // r$.complete()
  private void visitBatchBlock(JCEnhancedForLoop tree, Type batchType) {
    // protected Symtab syms = Symtab.instance(null);

    JavaToBatch trans = new JavaToBatch(types, syms);
    PExpr exp = tree.getStatement().accept(trans, JavaToBatch.DefaultContext);

    System.out.println("------BLOCK-----------------------");
    System.out.println(exp.toString());

    Environment env = new Environment(CodeModel.factory);
    env = env.extend(
      tree.var.sym.toString(),
      CodeModel.makeExtra(DynamicCallInfo.TYPE_INFO_KEY, tree.var.type),
      Place.REMOTE);
    History h = exp.partition(Place.MOBILE, env);

    System.out.println(h.toString());

    // create the factory variable
    //    VarSymbol factory = make.VarDef(null, 
    //    		names.fromString("f" + target.syntheticNameChar()), 
    //    		syms.batchFactoryType, currentMethodSym);
    //    JCStatement factorydef = make.VarDef(factory, tree.expr);

    // batch.util.BatchExpressionFactory f$ = <var>.getFactory();
    // batch.util.Forest s$ = new batch.util.Forest();
    // <prelocal>
    // batch.util.ForestReader r = <var>.execute(<remote>, s);
    // <postlocal>

    List<JCStatement> stats = List.nil();

    VarSymbol fact = new VarSymbol(0, names.fromString("f$"), batchType,
        currentMethodSym);
    VarSymbol send = new VarSymbol(0, names.fromString("s$"),
        syms.batchForestType, currentMethodSym);
    VarSymbol receive = new VarSymbol(0, names.fromString("r$"),
        syms.batchForestReaderType, currentMethodSym);

    Symbol ctor = lookupConstructor(tree.pos(), syms.batchForestType,
        List.<Type> nil());
    JCVariableDecl decl = make.VarDef(send,
        make.Create(ctor, List.<JCExpression> nil()));
    decl.setType(syms.batchForestType);
    stats = stats.append(decl);

    // batch.BatchFactory<batch.syntax.Expression> f$ = <service>;    
    // batch.util.Forest s$ = new batch.util.Forest();
    decl = make.VarDef(fact, tree.getExpression());
    decl.setType(batchType);
    stats = stats.append(decl);
    Type expressionType = fact.type.getTypeArguments().head;

    String rootName = tree.var.sym.toString();

    JCGenerator gen = new JCGenerator(context, currentMethodSym, fact,
        expressionType, attrEnv, rootName);
    gen.setInOut(null, send);
    JCStatement cmd;

    int step = 0;
    Stage s = h.get(step);
    if (s.place() == Place.LOCAL) {
      // local code using the forest
      stats = stats.append(s.action().runExtra(gen).generateStmt());
      step++;
    }
    if (step < h.length()) {
      s = h.get(step);
      if (s.place() == Place.REMOTE) {
        // batch.util.Forest r = <service>.execute(<remote>, s);

        gen.setInOut(send, null);
        JCExpression remote = s.action().runExtra(gen).generateRemote(); //in=send

        // TODO: HACK to get casts to work!
        JCExpression exec = gen.call(tree.getExpression(), "execute",
            syms.batchForestReaderType,
            make.TypeCast(expressionType, remote.setType(syms.objectType)),
            make.Ident(send));
        cmd = make.VarDef(receive, exec);
        cmd.setType(syms.batchForestReaderType);
        stats = stats.append(cmd);
        step++;
      }
      if (step < h.length()) {
        s = h.get(step);
        if (s.place() == Place.LOCAL) {
          // local code using the forest
          gen.setInOut(receive, null);
          stats = stats.append(s.action().runExtra(gen).generateStmt());
        }
      }
    }
    stats = stats.append(make.Exec(gen.call(
      make.Ident(receive),
      "complete",
      syms.voidType
    )));

    JCBlock block = make.Block(0, stats);
    block.endpos = TreeInfo.endPos(tree.body);
    result = translate(block);
    patchTargets(block, tree, result);
  }
}
