package com.sun.tools.javac.batch;

import java.util.ArrayList;

import batch.Op;
import batch.partition.CodeModel;
import batch.partition.PExpr;
import batch.partition.Place;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssignOp;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

class BatchTransformInfo {
  boolean returnAsExpr;
  Tree baseExp;

  public BatchTransformInfo() {
    this(false, null);
  }

  public BatchTransformInfo(boolean returnAsExpr) {
    this(returnAsExpr, null);
  }

  public BatchTransformInfo(boolean returnAsExpr, Tree baseExp) {
    super();
    this.returnAsExpr = returnAsExpr;
    this.baseExp = baseExp;
  }
}

public class JavaToBatch implements
    TreeVisitor<batch.partition.PExpr, BatchTransformInfo> {

  Types types;
  Symtab syms;

  public JavaToBatch(Types types, Symtab syms) {
    super();
    this.types = types;
    this.syms = syms;
  }

  static BatchTransformInfo DefaultContext = new BatchTransformInfo();

  static java.util.HashMap<String, String> mobileTypes;

  private PExpr checkMobile(Type type, PExpr exp) {
    String cls = javaToBatchType(type);
    if (cls != null) // type.isPrimitive())
      return CodeModel.factory.Mobile(cls, exp);
    else
      return exp;
  }

  private PExpr process(Tree base, BatchTransformInfo info) {
    if (base instanceof JCExpression) {
      JCExpression exp = (JCExpression) base;
      Type batchType = types.asSuper(types.upperBound(exp.type),
          syms.batchFunType.tsym);
      if (batchType != null)
        return CodeModel.factory.Data(base);
    }
    return base.accept(this, info);
  }

  String javaToBatchType(Type type) {
    if (mobileTypes == null) {
      mobileTypes = new java.util.HashMap<String, String>();
      mobileTypes.put("boolean", "Boolean");
      mobileTypes.put("java.lang.Boolean", "Boolean");
      mobileTypes.put("char", "Character");
      mobileTypes.put("java.lang.Character", "Character");
      mobileTypes.put("java.lang.String", "String");
      mobileTypes.put("byte", "Character");
      mobileTypes.put("java.lang.Byte", "Character");
      mobileTypes.put("short", "Integer");
      mobileTypes.put("java.lang.Short", "Integer");
      mobileTypes.put("int", "Integer");
      mobileTypes.put("java.lang.Integer", "Integer");
      mobileTypes.put("long", "Integer");
      mobileTypes.put("java.lang.Long", "Integer");
      mobileTypes.put("double", "Float");
      mobileTypes.put("java.lang.Double", "Float");
      mobileTypes.put("float", "Float");
      mobileTypes.put("java.lang.Float", "Float");
      mobileTypes.put("java.sql.Date", "Date");
      mobileTypes.put("java.sql.Time", "Time");
      mobileTypes.put("java.sql.Timestamp", "Timestamp");
      mobileTypes.put("java.util.Date", "UtilDate");
      mobileTypes.put("java.math.BigDecimal", "BigDecimal");
      mobileTypes.put("byte[]", "RawData");
    }
    String name = type.toString();
    String cls = mobileTypes.get(name);    
    return cls;
  }

  @Override
  public PExpr visitAnnotation(AnnotationTree node, BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  @Override
  public PExpr visitMethodInvocation(MethodInvocationTree node,
      BatchTransformInfo info) {
    JCMethodInvocation inv = (JCMethodInvocation) node;
    Tree meth = node.getMethodSelect();
    int numargs = node.getArguments().size();

    PExpr baseExp = null;
    MethodSymbol methSym = null;
    String methodNameString = null;
    if (meth instanceof JCFieldAccess) {
      JCFieldAccess fa = (JCFieldAccess) meth;
      methodNameString = fa.getIdentifier().toString();
      baseExp = process(fa.getExpression(), DefaultContext);
      methSym = (MethodSymbol) fa.sym;
    } else if (meth instanceof JCIdent) {
      JCIdent id = (JCIdent) meth;
      methodNameString = id.toString();
      methSym = (MethodSymbol) id.sym;
    }

    Class<batch.IncludeInBatch> annoType = batch.IncludeInBatch.class;
    batch.IncludeInBatch a = JavacElements.getAnnotation(methSym, annoType);
    if (a != null) {
      // batch method!
      java.util.List<PExpr> args = new java.util.ArrayList<PExpr>();
      for (int i = 0; i < numargs; i++)
        args.add(process(node.getArguments().get(i), DefaultContext));
      PExpr call = CodeModel.factory.DynamicCall(baseExp != null ? baseExp
          : CodeModel.factory.Other(null), methodNameString, args);
      call.setExtraInfo(getMethodInfo(methSym));

      return checkMobile(inv.type, call);
    } else if (baseExp != null) {
      java.util.List<PExpr> args = new java.util.ArrayList<PExpr>();
      for (int i = 0; i < numargs; i++)
        args.add(process(node.getArguments().get(i), DefaultContext));
      PExpr call = CodeModel.factory.Call(baseExp, methodNameString, args);
      call.setExtraInfo(inv.type);
      return checkMobile(inv.type, call);
    } else {
      Tree[] args = new Tree[1 + node.getArguments().size()];
      int i = 0;
      args[i++] = node.getMethodSelect();
      for (Tree x : node.getArguments())
        args[i++] = x;
      return other(node, args);
    }
  }

  @Override
  public PExpr visitAssert(AssertTree node, BatchTransformInfo info) {
    return other(node, node.getCondition(), node.getDetail());
  }

  @Override
  public PExpr visitAssignment(AssignmentTree node, BatchTransformInfo info) {
    // TODO: checkMobile
    return CodeModel.factory.Assign(process(node.getVariable(), info),
        process(node.getExpression(), info));
  }

  @Override
  public PExpr visitCompoundAssignment(CompoundAssignmentTree node,
      BatchTransformInfo info) {
    JCAssignOp ass = (JCAssignOp)node;
    Op op = transAssignOp(ass.getTag());
    if (op != null) {
      PExpr a = process(ass.getVariable(), DefaultContext);
      PExpr b = process(ass.getExpression(), DefaultContext);
      PExpr result = CodeModel.factory.Assign(a, CodeModel.factory.Prim(op, a, b));
      return checkMobile(ass.type, result);
    } else
      return other(node, ass.getVariable(), ass.getExpression());
  }

  @Override
  public PExpr visitBinary(BinaryTree node, BatchTransformInfo info) {
    JCBinary bin = (JCBinary) node;

    Op op = getBinaryOp(bin.getTag());
    if (op != null) {
      java.util.List<PExpr> args = new java.util.ArrayList<PExpr>();
      args.add(process(node.getLeftOperand(), DefaultContext));
      args.add(process(node.getRightOperand(), DefaultContext));
      PExpr result = CodeModel.factory.Prim(op, args);
      return checkMobile(bin.type, result);
    } else
      return other(node, node.getLeftOperand(), node.getRightOperand());
  }

  Op getBinaryOp(int op) {
    switch (op) {
    case JCTree.OR:
      return batch.Op.OR;
    case JCTree.AND:
      return batch.Op.AND;
    case JCTree.EQ:
      return batch.Op.EQ;
    case JCTree.NE:
      return batch.Op.NE;
    case JCTree.LT:
      return batch.Op.LT;
    case JCTree.GT:
      return batch.Op.GT;
    case JCTree.LE:
      return batch.Op.LE;
    case JCTree.GE:
      return batch.Op.GE;
    case JCTree.PLUS:
      return batch.Op.ADD;
    case JCTree.MINUS:
      return batch.Op.SUB;
    case JCTree.MUL:
      return batch.Op.MUL;
    case JCTree.DIV:
      return batch.Op.DIV;
    case JCTree.MOD:
      return batch.Op.MOD;
    }
    return null;
  }
  
  private Op transAssignOp(int op) {
    switch (op) {
    case JCTree.MUL_ASG:
      return Op.MUL;
    case JCTree.DIV_ASG:
      return Op.DIV;
    case JCTree.MOD_ASG:
      return Op.MOD;
    case JCTree.PLUS_ASG:
      return Op.ADD;
    case JCTree.MINUS_ASG:
      return Op.SUB;
    }
    return null;
  }


  @Override
  public PExpr visitBlock(BlockTree node, BatchTransformInfo info) {
    java.util.List<PExpr> items = new ArrayList<PExpr>();
    int count = node.getStatements().size();
    for (int i = count; i-- > 0;) {
      StatementTree stmt = node.getStatements().get(i);
      if (stmt.getKind() == Tree.Kind.VARIABLE) {
        JCVariableDecl decl = (JCVariableDecl) stmt;
        PExpr init = process(decl.getInitializer(), DefaultContext);
        PExpr let = CodeModel.factory.Let(decl.getName().toString(), init,
            makeSeq(items)).setExtraInfo(decl);
        items = new java.util.ArrayList<PExpr>();
        items.add(let);
      } else {
        // only ignore return on last statement!
        items.add(0, process(stmt, new BatchTransformInfo(i == count - 1)));
      }
    }
    return makeSeq(items);
  }

  // default to remote
  Place getPlace(Symbol sym) {
    String batchType = javaToBatchType(sym.type);
    return (batchType != null || JavacElements.getAnnotation(sym,
        batch.Local.class) != null) ? Place.LOCAL : Place.REMOTE;
  }

  public JCDynamicCallInfo getMethodInfo(MethodSymbol meth) {
    java.util.List<Place> argInfo = new java.util.ArrayList<Place>();
    for (VarSymbol p : meth.getParameters())
      argInfo.add(getPlace(p.owner));
    return new JCDynamicCallInfo(
    // todo: what about mobile types?
        getPlace(meth), argInfo, meth);
  }

  PExpr makeSeq(java.util.List<PExpr> items) {
    if (items.size() == 1)
      return items.get(0);
    else {
      return CodeModel.factory.Prim(Op.SEQ, items);
    }
  }

  @Override
  public PExpr visitBreak(BreakTree node, BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitCase(CaseTree node, BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  @Override
  public PExpr visitCatch(CatchTree node, BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  @Override
  public PExpr visitClass(ClassTree node, BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitConditionalExpression(ConditionalExpressionTree node,
      BatchTransformInfo info) {
    JCConditional cond = (JCConditional) node;
    PExpr result = CodeModel.factory.If(
        process(node.getCondition(), DefaultContext),
        process(node.getTrueExpression(), DefaultContext),
        process(node.getFalseExpression(), DefaultContext));
    return checkMobile(cond.type, result);
  }

  @Override
  public PExpr visitContinue(ContinueTree node, BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitDoWhileLoop(DoWhileLoopTree node, BatchTransformInfo info) {
    return other(node, node.getCondition(), node.getStatement());
  }

  @Override
  public PExpr visitErroneous(ErroneousTree node, BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  @Override
  public PExpr visitExpressionStatement(ExpressionStatementTree node,
      BatchTransformInfo info) {
    return process(node.getExpression(), DefaultContext);
  }

  @Override
  public PExpr visitEnhancedForLoop(EnhancedForLoopTree node,
      BatchTransformInfo info) {
    return CodeModel.factory.Loop(node.getVariable().getName().toString(),
        process(node.getExpression(), DefaultContext),
        process(node.getStatement(), DefaultContext));
  }

  @Override
  public PExpr visitForLoop(ForLoopTree node, BatchTransformInfo info) {
    Tree[] args = new Tree[2 + node.getInitializer().size()
        + node.getUpdate().size()];
    int i = 0;
    for (Tree x : node.getInitializer())
      args[i++] = x;
    args[i++] = node.getCondition();
    for (Tree x : node.getUpdate())
      args[i++] = x;
    args[i++] = node.getStatement();
    return other(node, args);
  }

  @Override
  public PExpr visitIdentifier(IdentifierTree node, BatchTransformInfo info) {
    JCIdent id = (JCIdent) node;
    PExpr result = CodeModel.factory.Var(node.getName().toString());
    result.setExtraInfo(id.sym);
    return checkMobile(id.type, result);
  }

  @Override
  public PExpr visitIf(IfTree node, BatchTransformInfo info) {
    PExpr b = process(node.getThenStatement(), DefaultContext);
    PExpr c = node.getElseStatement() == null ? CodeModel.factory.Skip()
        : process(node.getElseStatement(), DefaultContext);

    return CodeModel.factory.If(process(node.getCondition(), DefaultContext),
        b, c);
  }

  @Override
  public PExpr visitImport(ImportTree node, BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  @Override
  public PExpr visitArrayAccess(ArrayAccessTree node, BatchTransformInfo info) {
    return other(node, node.getExpression(), node.getIndex());
  }

  @Override
  public PExpr visitLabeledStatement(LabeledStatementTree node,
      BatchTransformInfo info) {
    return other(node, node.getStatement());
  }

  @Override
  public PExpr visitLiteral(LiteralTree node, BatchTransformInfo info) {
    return CodeModel.factory.Data(node.getValue());
  }

  @Override
  public PExpr visitModifiers(ModifiersTree node, BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  @Override
  public PExpr visitNewArray(NewArrayTree node, BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  @Override
  public PExpr visitNewClass(NewClassTree node, BatchTransformInfo info) {
    Tree[] args = new Tree[node.getArguments().size()];
    int i = 0;
    for (Tree x : node.getArguments())
      args[i++] = x;
    return other(node, args);
  }

  @Override
  public PExpr visitParenthesized(ParenthesizedTree node,
      BatchTransformInfo info) {
    return process(node.getExpression(), info);
  }

  @Override
  public PExpr visitReturn(ReturnTree node, BatchTransformInfo info) {
    if (info.returnAsExpr)
      // true is a special flag that "return" was deleted
      return CodeModel.factory.Mobile(null,
          process(node.getExpression(), DefaultContext));
    else
      return other(node, node.getExpression());
  }

  @Override
  public PExpr visitMemberSelect(MemberSelectTree node, BatchTransformInfo info) {
    JCFieldAccess fld = (JCFieldAccess) node;
    PExpr part = process(node.getExpression(), DefaultContext);
    //TODO!!     if (!(part instanceof CodeModel.Other))
    PExpr result = CodeModel.factory
        .Prop(part, node.getIdentifier().toString());
    result.setExtraInfo(fld.type);
    // HACK: The problem is that generic parameters do not have 
    // specific types.. and I can't figure out how to find them. Help!
    return checkMobile(/*HACK!*/fld.name.toString().equals("Key") ? syms.stringType : fld.type, result);
  }

  @Override
  public PExpr visitEmptyStatement(EmptyStatementTree node,
      BatchTransformInfo info) {
    return CodeModel.factory.Skip();
  }

  @Override
  public PExpr visitSwitch(SwitchTree node, BatchTransformInfo info) {
    java.util.List<PExpr> args = new java.util.ArrayList<PExpr>();
    args.add(process(node.getExpression(), info));
    for (CaseTree x : node.getCases()) {
      args.add(process(x.getExpression(), info));
      if (x.getStatements().size() == 1)
        args.add(process(x.getStatements().get(0), info));
      else {
        java.util.List<PExpr> stats = new java.util.ArrayList<PExpr>();
        for (Tree n : x.getStatements())
          stats.add(process(n, DefaultContext));
        args.add(makeSeq(stats));
      }
    }
    return CodeModel.factory.Other(node, args);
  }

  @Override
  public PExpr visitSynchronized(SynchronizedTree node, BatchTransformInfo info) {
    return other(node, node.getExpression(), node.getBlock());
  }

  @Override
  public PExpr visitThrow(ThrowTree node, BatchTransformInfo info) {
    return other(node, node.getExpression());
  }

  @Override
  public PExpr visitCompilationUnit(CompilationUnitTree node,
      BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  @Override
  public PExpr visitTry(TryTree node, BatchTransformInfo info) {
    Tree[] args = new Tree[2 + node.getCatches().size()];
    int i = 0;
    args[i++] = node.getBlock();
    args[i++] = node.getFinallyBlock();
    for (CatchTree x : node.getCatches())
      args[i++] = x.getBlock();
    return other(node, args);
  }

  @Override
  public PExpr visitParameterizedType(ParameterizedTypeTree node,
      BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitUnionType(UnionTypeTree node, BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitArrayType(ArrayTypeTree node, BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitTypeCast(TypeCastTree node, BatchTransformInfo info) {
    PExpr exp = process(node.getExpression(), DefaultContext);
    return CodeModel.factory.Mobile(null, exp);
  }

  @Override
  public PExpr visitPrimitiveType(PrimitiveTypeTree node,
      BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitTypeParameter(TypeParameterTree node,
      BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitInstanceOf(InstanceOfTree node, BatchTransformInfo info) {
    return other(node, node.getExpression());
  }

  @Override
  public PExpr visitUnary(UnaryTree node, BatchTransformInfo info) {
    JCUnary un = (JCUnary) node;

    PExpr exp = getUnaryOp(un.getTag(), null);
    if (exp != null) {
      PExpr result = getUnaryOp(un.getTag(), process(node.getExpression(), DefaultContext));
      return checkMobile(un.type, result);
    } else
      return other(node, node.getExpression());
  }

  PExpr getUnaryOp(int op, PExpr exp) {
    PExpr one = CodeModel.factory.Data(1);
    switch (op) {
    case JCTree.POS:
      return CodeModel.factory.Prim(batch.Op.ADD, exp);
    case JCTree.NEG:
      return CodeModel.factory.Prim(batch.Op.SUB, exp);
    case JCTree.NOT:
      return CodeModel.factory.Prim(batch.Op.NOT, exp);

    case JCTree.PREINC:
      return CodeModel.factory.Assign(exp, CodeModel.factory.Prim(batch.Op.ADD, exp, one));
    case JCTree.PREDEC:
      return CodeModel.factory.Assign(exp, CodeModel.factory.Prim(batch.Op.SUB, exp, one));
    case JCTree.POSTINC:
      return CodeModel.factory.Prim(Op.SUB, CodeModel.factory.Assign(exp, CodeModel.factory.Prim(batch.Op.ADD, exp, one)), one);
    case JCTree.POSTDEC:
      return CodeModel.factory.Prim(Op.ADD, CodeModel.factory.Assign(exp, CodeModel.factory.Prim(batch.Op.SUB, exp, one)), one);
    }
    return null;
  }

  @Override
  public PExpr visitVariable(VariableTree node, BatchTransformInfo info) {
    if (info.baseExp != null) {
      //  if (isFieldAccess()) {
      PExpr part = process(info.baseExp, DefaultContext);
      //TODO!!     if (!(part instanceof CodeModel.Other))
      return CodeModel.factory.Prop(part, node.getName().toString());
      //  }
      //  return null; // super.toBatchSub(returnAsExpr);
    } else
      return CodeModel.factory.Var(node.getName().toString());
  }

  @Override
  public PExpr visitWhileLoop(WhileLoopTree node, BatchTransformInfo info) {
    return other(node, node.getCondition(), node.getStatement());
  }

  @Override
  public PExpr visitWildcard(WildcardTree node, BatchTransformInfo info) {
    return other(node);
  }

  @Override
  public PExpr visitOther(Tree node, BatchTransformInfo info) {
    throw new Error("MISSING");
  }

  PExpr other(Tree base, Tree... parts) {
    if (base instanceof JCExpression) {
      JCExpression exp = (JCExpression) base;
      Type batchType = types.asSuper(types.upperBound(exp.type),
          syms.batchFunType.tsym);
      if (batchType != null)
        return CodeModel.factory.Data(this);
    }

    if (parts.length == 0)
      return CodeModel.factory.Other(base);
    java.util.List<PExpr> args = new java.util.ArrayList<PExpr>();
    for (Tree n : parts)
      args.add(process(n, DefaultContext));
    Type type = ((JCTree) base).type;
    return checkMobile(type, CodeModel.factory.Other(base, args));
  }

  @Override
  public PExpr visitMethod(MethodTree node, BatchTransformInfo p) {
    throw new Error("SHOULD NEVER BE CALLED");
  }

}
