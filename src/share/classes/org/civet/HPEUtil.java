package org.civet;

import java.lang.reflect.Array;

import org.civet.HPEObject.Tag;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.TreeVisitor;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class HPEUtil {

  static List<JCMethodDecl> findTaggedMethods(JCClassDecl classDecl) {
    ListBuffer<JCMethodDecl> taggedMethods = new ListBuffer<JCTree.JCMethodDecl>();
    for (JCTree d : classDecl.defs) {
      if (d.getKind() == Kind.METHOD) {
        JCMethodDecl md = null;
        md = (JCMethodDecl) d;
        for (JCAnnotation a : md.getModifiers().annotations) {
          if (isCivetCompileTag(a)) {
            taggedMethods.append(md);
          }
        }
      }
    }
    return taggedMethods.toList();
  }

  static Boolean isCivetCompileTag(JCAnnotation annotation) {
    return annotation.type.tsym.getQualifiedName().toString()
        .equals("org.civet.Civet.Compile");
  }

  static JCBlock newBlock(long flags, List<? extends Tree> stats, HPEContext p) {
    List<? extends Tree> filtered = filterEmptyStmt(stats);
    if (filtered.length() == 1 && filtered.get(0).getKind() == Kind.BLOCK
        && ((JCBlock) filtered.get(0)).flags == flags) {
      return (JCBlock) filtered.get(0);
    }
    return p.treeMaker.Block(flags,
        Function.map(new Function<JCStatement, Tree>() {
          @Override
          public JCStatement apply(Tree t) {
            return (JCStatement) t;
          }
        }, filtered));
  }

  static <T extends Tree> List<T> filterEmptyStmt(List<T> l) {
    return Function.filter(new Function<Boolean, T>() {
      @Override
      public Boolean apply(Tree t) {
        return isEmptyStmt(t);
      }
    }, l);
  }

  static Boolean isEmptyStmt(Tree t) {
    if (t.getKind() == Kind.BLOCK) {
      JCBlock block = (JCBlock) t;
      return block.getStatements().length() == 0;
    } else
      return t.getKind() == Kind.EMPTY_STATEMENT;
  }

  static HPEObject binOp(Kind op, HPEObject lhs, HPEObject rhs)
      throws HPEException {
    switch (op) {
      case PLUS_ASSIGNMENT:
      case PLUS:
        return MathUtil.plus(lhs, rhs);
      case DIVIDE_ASSIGNMENT:
      case DIVIDE:
        return MathUtil.div(lhs, rhs);
      case MULTIPLY_ASSIGNMENT:
      case MULTIPLY:
        return MathUtil.mult(lhs, rhs);
      case MINUS_ASSIGNMENT:
      case MINUS:
        return MathUtil.minus(lhs, rhs);
      case LESS_THAN:
        return MathUtil.lt(lhs, rhs);
      case LESS_THAN_EQUAL:
        return MathUtil.le(lhs, rhs);
      case GREATER_THAN:
        return MathUtil.gt(lhs, rhs);
      case GREATER_THAN_EQUAL:
        return MathUtil.ge(lhs, rhs);
      case EQUAL_TO:
        return MathUtil.eq(lhs, rhs);
      case NOT_EQUAL_TO:
        return MathUtil.ne(lhs, rhs);
      case REMAINDER_ASSIGNMENT:
      case REMAINDER:
        return MathUtil.mod(lhs, rhs);
      case LEFT_SHIFT_ASSIGNMENT:
      case LEFT_SHIFT:
        return MathUtil.sl(lhs, rhs);
      case RIGHT_SHIFT_ASSIGNMENT:
      case RIGHT_SHIFT:
        return MathUtil.sr(lhs, rhs);
      case UNSIGNED_RIGHT_SHIFT_ASSIGNMENT:
      case UNSIGNED_RIGHT_SHIFT:
        return MathUtil.usr(lhs, rhs);
      case AND_ASSIGNMENT:
      case AND:
        return MathUtil.bitand(lhs, rhs);
      case OR_ASSIGNMENT:
      case OR:
        return MathUtil.bitor(lhs, rhs);
      case XOR_ASSIGNMENT:
      case XOR:
        return MathUtil.bitxor(lhs, rhs);
      case BITWISE_COMPLEMENT:
        return MathUtil.bitnot(lhs);
      case CONDITIONAL_OR:
        return MathUtil.or(lhs, rhs);
      case CONDITIONAL_AND:
        return MathUtil.and(lhs, rhs);
      case LOGICAL_COMPLEMENT:
        return MathUtil.not(lhs);
      default:
        throw new NotImplementedException();
    }
  }

  static HPEObject unaryOp(JCUnary unary, HPEObject result, HPEContext p) {
    HPEObject nv;
    HPEObject ret;
    switch (unary.getTag()) {
      case JCTree.POSTINC: // _ ++
        nv = MathUtil.inc(result);
        p.env.add(result.symbol, nv.setTag(result.tag));
        ret = result;
        return ret;
      case JCTree.POSTDEC: // _ --
        nv = MathUtil.dec(result);
        p.env.add(result.symbol, nv.setTag(result.tag));
        ret = result;
        return ret;
        // Unary operators
      case JCTree.PREINC: // ++ _
        nv = MathUtil.inc(result);
        p.env.add(result.symbol, nv.setTag(result.tag));
        return result;
      case JCTree.PREDEC: // -- _
        nv = MathUtil.dec(result);
        p.env.add(result.symbol, nv.setTag(result.tag));
        return result;
      case JCTree.POS: // +
        return result;
      case JCTree.NEG: // -
        return MathUtil.neg(result);
      case JCTree.NOT: // -
        return MathUtil.not(result);
      default:
        return null;
    }
  }

  static Action add(Action a1, Action a2) {
    if (a1 == a2)
      return a1;
    else if (a1 == Action.INVOKE_OR_CODE)
      return a2;
    else if (a2 == Action.INVOKE_OR_CODE)
      return a1;
    else if (a1 == Action.INVOKE_OR_SPECIALIZE
        || a2 == Action.INVOKE_OR_SPECIALIZE) {
      Action otherAction = a1 == Action.INVOKE_OR_SPECIALIZE ? a2 : a1;
      if (otherAction == Action.CODE)
        return Action.SPECIALIZE;
      else
        return otherAction;
    } else if (a1 == Action.INVOKE_OR_ERROR || a2 == Action.INVOKE_OR_ERROR) {
      Action otherAction = a1 == Action.INVOKE_OR_ERROR ? a2 : a1;
      if (otherAction == Action.CODE)
        return Action.ERROR;
      else
        return Action.INVOKE_OR_ERROR;
    } else if (a1 == Action.SPECIALIZE || a2 == Action.SPECIALIZE)
      return Action.SPECIALIZE;
    else if (a1 == Action.ERROR || a2 == Action.ERROR)
      return Action.ERROR;
    else
      throw new HPEException("Invalid action combination.");
  }

  static JCLiteral makeLiteral(HPEObject o, HPEContext p) {
    if (o.kind == HPEObject.Kind.NULL) {
      return p.treeMaker.Literal(TypeTags.BOT, o.object);
    }
    return p.treeMaker.Literal(o.object);
  }

  static Action findActionForCall(List<HPEResult> args, CLOSURE closure,
      HPEContext p) {
    Action action = null;
    ListBuffer<HPEObject> params = new ListBuffer<>();
    if (!closure.methodSymbol.isStatic()) {
      params.append(closure.thiz);
    }

    for (HPEResult r : args) {
      params.append(r.value());
    }
    ClassSymbol classSymbol = (ClassSymbol) closure.methodSymbol.owner;
    action = sumActions(params.toList(), p.hasSource(classSymbol));
    return action;
  }

  static Action sumActions(List<HPEObject> params, Boolean sourceAvilable) {
    ListBuffer<Action> actionBuffer = new ListBuffer<>();
    for (HPEObject param : params) {
      actionBuffer.append(getAction(param, sourceAvilable));
    }
    List<Action> actions = actionBuffer.toList();
    Action resultAction = Action.CODE;
    if (actions.length() > 0) {
      resultAction = actions.head;
      for (Action ac : actions.tail) {
        resultAction = HPEUtil.add(resultAction, ac);
      }
    }
    return resultAction;
  }

  static Action getAction(HPEObject param, Boolean sourceAvilable) {
    if (sourceAvilable) {
      if (param.isConcrete())
        return Action.INVOKE_OR_SPECIALIZE;
      else
        return Action.CODE;
    } else {
      if (param.isConcrete())
        if (param.isPrimitive())
          return Action.INVOKE_OR_CODE;
        else
          return Action.INVOKE_OR_ERROR;
      else
        return Action.CODE;
    }
  }

  static HPEObject resolveSymbol(Symbol sym, HPEObject thiz, HPEContext p) {
    switch (sym.getKind()) {
      case METHOD:
        if (sym.isStatic()) {
          CLASS c = new CLASS((ClassSymbol) sym.owner);
          return new CLOSURE(c.setTag(Tag.ABSTRACT), (MethodSymbol) sym)
              .setTag(Tag.ABSTRACT);
        } else {
          if (thiz.isConcrete()) {
            ClassSymbol thizClass = (ClassSymbol) p
                .findSymbolForClass(thiz.object.getClass());
            MethodSymbol actualMethod = p.findMethodSymbolInClassSymbol(
                (MethodSymbol) sym, thizClass);
            return new CLOSURE(thiz, actualMethod).setTag(Tag.CONCRETE);
          }
          return new CLOSURE(thiz, (MethodSymbol) sym).setTag(Tag.ABSTRACT);
        }
      case ENUM:
      case CLASS:
        HPEObject ret = new CLASS((ClassSymbol) sym).setTag(Tag.ABSTRACT);
        p.env.add(HPESymbol.local(sym.name), ret); // FIXME: ?? local?
        ret.symbol = HPESymbol.local(sym.name);
        return ret;
      case LOCAL_VARIABLE:
      case PARAMETER:
        HPEObject retp = p.env.lookup(HPESymbol.local(sym.name));
        retp.symbol = HPESymbol.local(sym.name);
        return retp;
      case FIELD:
        if (sym.name.equals(p.names._this)) {
          return thiz;
        }
        HPEObject fv = thiz.getFieldValue(sym.name);
        fv.symbol = HPESymbol.fromHPEObject(fv);
        return fv;

      case ENUM_CONSTANT:
        ClassSymbol enumClassSymbol = (ClassSymbol) sym.owner;
        Class<?> enumClass = toClass(enumClassSymbol.type);
        for (Object x : enumClass.getEnumConstants()) {
          if (x.toString().equals(sym.name.toString())) {
            HPEObject enumO = new ObjectLiteral(x, false);
            enumO.isEnum = true;
            return enumO;
          }
        }
        return null;
        
      case CONSTRUCTOR:
        return new CLOSURE(thiz, (MethodSymbol) sym).setTag(Tag.ABSTRACT);        
      default:
        throw new HPEException("Invalid state!");
    }
  }

  static <T extends Tree> List<T> unboxHPEResults(List<HPEResult> l) {
    return Function.map(new Function<T, HPEResult>() {
      @SuppressWarnings("unchecked")
      @Override
      public T apply(HPEResult t) {
        return (T) t.code;
      }
    }, l);
  }

  static <T extends Tree> List<HPEResult> hpeList(List<T> l,
      final TreeVisitor<HPEResult, HPEContext> visitor, final HPEContext p) {
    return Function.map(new Function<HPEResult, T>() {
      @Override
      public HPEResult apply(T t) {
        return t.accept(visitor, p);
      }
    }, l);
  }

  public static Class<?> toClass(Type type) {
    switch (type.tag) {
      case TypeTags.ARRAY:
        ArrayType arrayType = (ArrayType) type;
        Class<?> claz = toClass(arrayType.elemtype);
        return Array.newInstance(claz, 0).getClass();
      case TypeTags.BOOLEAN:
        return Boolean.TYPE;
      case TypeTags.BYTE:
        return Byte.TYPE;
      case TypeTags.CHAR:
        return Character.TYPE;
      case TypeTags.DOUBLE:
        return Double.TYPE;
      case TypeTags.FLOAT:
        return Float.TYPE;
      case TypeTags.INT:
        return Integer.TYPE;
      case TypeTags.LONG:
        return Long.TYPE;
      case TypeTags.SHORT:
        return Short.TYPE;
      default:
        try {
          return Class.forName(type.tsym.flatName().toString());
        } catch (ClassNotFoundException e) {
          throw new HPEException(e);
        }
    }
  }

}

enum Action {
  ERROR, CODE, SPECIALIZE, INVOKE, INVOKE_OR_CODE, INVOKE_OR_SPECIALIZE, INVOKE_OR_ERROR
}
