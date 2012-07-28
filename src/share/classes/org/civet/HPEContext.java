package org.civet;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.jvm.ClassReader.SourceCompleter;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Filter;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Pair;

public class HPEContext {
  public HPEEnvironment env;
  public Context javacContext;
  public TreeMaker treeMaker;
  public Types types;
  public Names names;
  public Trees trees;
  public SourceCompleter sourceCompleter;
  public Symtab symtab;

  public HPEContext(HPEEnvironment env, Context javacContext) {
    super();
    this.env = env;
    this.javacContext = javacContext;
    this.treeMaker = TreeMaker.instance(javacContext);
    this.types = Types.instance(javacContext);
    this.names = Names.instance(javacContext);
    this.trees = JavacTrees.instance(javacContext);
    this.symtab = Symtab.instance(javacContext);
  }

  private List<Method> genMethods = List.<Method> nil();
  private Set<String> methodNames = new HashSet<String>();

  Pair<Method, Boolean> generateName(MethodSymbol methodSymbol,
      List<Parameter> staticArgs) {

    // Look if a name for this call has already been generated.
    Method m = new Method(methodSymbol, staticArgs, this);
    int i = genMethods.indexOf(m);

    if (i >= 0) // Already generated.
      return Pair.of(genMethods.get(i), true);

    // Generate a new name.
    String genName = "";
    do {
      if (methodSymbol.getKind() == ElementKind.CONSTRUCTOR) {
        genName = methodSymbol.owner.name.toString() + "$" + getNextNameSeqNum();
      }
      else {
        genName = methodSymbol.name.toString() + "$" + getNextNameSeqNum();
      }
    } while (methodNames.contains(genName));

    methodNames.add(genName);
    genMethods = genMethods.append(m.setGenMethodName(genName));

    return Pair.of(m, false);
  }

  private static Integer nameSeqNum = new Integer(1000);

  private static String getNextNameSeqNum() {
    nameSeqNum++;
    return String.valueOf(nameSeqNum);
  }

  boolean equalsMethodSym(MethodSymbol m1, MethodSymbol m2) {
    return m1.name.toString().equals(m2.name.toString())
        && this.types.isSameType(m1.type, m2.type)
        && (this.types.isSameType(m1.owner.type, m2.owner.type));
  }

  boolean equalsMethodSignature(MethodSymbol m1, MethodSymbol m2) {
    return m1.name.toString().equals(m2.name.toString())
        && this.types.isSameType(m1.type, m2.type);
  }

  MethodSymbol retMethodSymbol(MethodInvocationTree node) {
    JCMethodInvocation mi = (JCMethodInvocation) node;
    if (mi.meth.getKind() == Kind.MEMBER_SELECT) {
      JCFieldAccess methAccess = (JCFieldAccess) mi.meth;
      return (MethodSymbol) methAccess.sym;
    } else if (mi.meth.getKind() == Kind.IDENTIFIER) {
      JCIdent methAccess = (JCIdent) mi.meth;
      return (MethodSymbol) methAccess.sym;
    }
    return null;
  }

  MethodSymbol generateMethodSymbol(MethodSymbol ms, Name newName,
      List<Type> rtTypes, List<Parameter> ctParams, boolean isStatic) {
    assert (ms.owner.getKind() == ElementKind.CLASS || ms.owner.getKind() == ElementKind.INTERFACE);
    MethodType mt = new MethodType(rtTypes, ms.getReturnType(),
        ms.getThrownTypes(), ms.type.tsym);
    long flags = isStatic ? ms.flags_field | Flags.STATIC : ms.flags_field;
    MethodSymbol genMS = new MethodSymbol(flags, newName, mt, ms.owner);
    genMS.params = List.<VarSymbol> nil();
    for (int i = 0; i < ms.params.length(); i++) {
      VarSymbol pv = ms.params.get(i);
      boolean isCT = false;
      for (Parameter p : ctParams) {
        if (p.position == i) {
          isCT = true;
          break;
        }
      }
      if (!isCT) {
        genMS.params = genMS.params.append(pv);
      }
    }
    return genMS;
  }

  JCMethodInvocation generateStaticMethodInvocation(MethodSymbol ms,
      List<JCExpression> args) {
    assert (ms.params.length() == args.length());
    // ClassSymbol clazz = (ClassSymbol) ms.owner;
    JCMethodInvocation mi = treeMaker.App(
        treeMaker.Select(treeMaker.Ident(ms.owner.getQualifiedName()), ms),
        args);
    return mi;
  }

  JCMethodInvocation generateInstanceMethodInvocation(JCExpression selected,
      MethodSymbol ms, List<JCExpression> args) {
    JCMethodInvocation mi = treeMaker.App(treeMaker.Select(selected, ms), args);
    return mi;
  }

  JCMethodInvocation generateInstanceMethodInvocation(MethodSymbol ms,
      List<JCExpression> args) {
    JCMethodInvocation mi = treeMaker.App(treeMaker.Ident(ms), args);
    return mi;
  }

  boolean hasSource(Symbol s) {
    return trees.getTree(s) != null;
  }

  List<ClassSymbol> subTypes(Symbol c) {
    ListBuffer<ClassSymbol> subs = new ListBuffer<>();
    ClassSymbol[] classes = symtab.classes.values().toArray(
        new ClassSymbol[] {});
    for (int i = 0; i < classes.length; i++) {
      if (hasSource(classes[i]) && classes[i].isSubClass(c, types)) {
        subs.append(classes[i]);
      }
    }
    return subs.toList();
  }

  TypeSymbol findSymbolForClass(Class<?> clazz) {
    String className = clazz.getName();
    if (clazz.isPrimitive()) {
      switch (clazz.getName()) {
        case "int":
          return findSymbolForClass(Integer.class);
        case "short":
          return findSymbolForClass(Short.class);
        case "long":
          return findSymbolForClass(Long.class);
        case "float":
          return findSymbolForClass(Float.class);
        case "double":
          return findSymbolForClass(Double.class);
        case "byte":
          return findSymbolForClass(Byte.class);
        case "char":
          return findSymbolForClass(Character.class);
        default:
          throw new HPEException("not implemented for " + clazz);
      }
    }
    return symtab.classes.get(names.fromString(className));
  }

  MethodSymbol findMethodSymbolInClassSymbol(final MethodSymbol m, ClassSymbol c) {
    Iterable<Symbol> elems = c.members().getElements(new Filter<Symbol>() {
      @Override
      public boolean accepts(Symbol t) {
        return t instanceof MethodSymbol
            && t.name.toString().equals(m.name.toString())
            && HPEContext.this.types.isSameType(t.type, m.type);
      }
    });
    if (elems.iterator().hasNext()) {
      return (MethodSymbol) elems.iterator().next();
    } else {
      if (c.getSuperclass().getKind() == TypeKind.NONE) {
        return m;
      } else {
        return findMethodSymbolInClassSymbol(m,
            (ClassSymbol) c.getSuperclass().tsym);
      }
    }
  }

  Type getType(Class<?> clazz) {
    if (clazz.isArray()) {
      return new Type.ArrayType(getType(clazz.getComponentType()),
          symtab.arrayClass);
    }
    return findSymbolForClass(clazz).type;
  }

}
