/*******************************************************************************
 * The accompanying source code is made available to you under the terms of 
 * the UT Research License (this "UTRL"). By installing or using the code, 
 * you are consenting to be bound by the UTRL. See LICENSE.html for a 
 * full copy of the license.
 * 
 * Copyright @ 2009, The University of Texas at Austin. All rights reserved.
 * 
 * UNIVERSITY EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES CONCERNING THIS 
 * SOFTWARE AND DOCUMENTATION, INCLUDING ANY WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR ANY PARTICULAR PURPOSE, NON-INFRINGEMENT AND WARRANTIES 
 * OF PERFORMANCE, AND ANY WARRANTY THAT MIGHT OTHERWISE ARISE FROM COURSE 
 * OF DEALING OR USAGE OF TRADE. NO WARRANTY IS EITHER EXPRESS OR IMPLIED 
 * WITH RESPECT TO THE USE OF THE SOFTWARE OR DOCUMENTATION. Under no circumstances 
 * shall University be liable for incidental, special, indirect, direct 
 * or consequential damages or loss of profits, interruption of business, 
 * or related expenses which may arise from use of Software or Documentation, 
 * including but not limited to those resulting from defects in Software 
 * and/or Documentation, or loss or inaccuracy of data of any kind.
 * 
 * Created by: William R. Cook and Eli Tilevich
 * with: Jose Falcon, Marc Fisher II, Ali Ibrahim, Yang Jiao, Ben Wiedermann
 * University of Texas at Austin and Virginia Tech
 ******************************************************************************/
package com.sun.tools.javac.batch;

import batch.Op;
import batch.partition.PartitionFactoryHelper;
import batch.partition.Place;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

// FACTORY
public class JCGenerator extends PartitionFactoryHelper<Generator> {
  protected TreeMaker make;
  protected Names names;
  protected Symtab syms;
  private Symbol factorySym;
  private Resolve rs;
  JCGenerator factory;
  private Env<AttrContext> attrEnv;
  protected Symbol currentMethodSym;
  private Type expressionType;
  private BatchToJava conv;
  Symbol in;
  Symbol out;
  final String root;

  public JCGenerator(Context context, Symbol currentMethodSym,
      Symbol factorySym, Type expressionType, Env<AttrContext> attrEnv,
      String root) {
    make = TreeMaker.instance(context);
    names = Names.instance(context);
    syms = Symtab.instance(context);
    rs = Resolve.instance(context);
    this.currentMethodSym = currentMethodSym;
    conv = new BatchToJava(context);

    this.factorySym = factorySym;
    this.expressionType = expressionType;
    this.attrEnv = attrEnv;
    this.in = null;
    this.out = null;
    this.root = root;
  }

  public void setInOut(Symbol in, Symbol out) {
    this.in = in;
    this.out = out;
  }

  private JCExpression factoryId() {
    return make.Ident(factorySym);
  }

  protected JCExpression makeOpLiteral(Op op) {
    //  args.add(new Dot(new TypeAccess("batch", "Op"), new VarAccess(op
    //      .toString())));
    VarSymbol sym = rs.resolveInternalField(null, attrEnv, syms.batchOpType,
        names.fromString(op.toString()));
    return make.Select(make.Type(syms.batchOpType), sym).setType(
        syms.batchOpType);
  }

  private int transOp(Op op) {
    switch (op) {
    case MUL:
      return JCTree.MUL;
    case DIV:
      return JCTree.DIV;
    case MOD:
      return JCTree.MOD;
    case ADD:
      return JCTree.PLUS;
    case SUB:
      return JCTree.MINUS;
    case LT:
      return JCTree.LT;
    case GT:
      return JCTree.GT;
    case LE:
      return JCTree.LE;
    case GE:
      return JCTree.GE;
    case EQ:
      return JCTree.EQ;
    case NE:
      return JCTree.NE;
    case AND:
      return JCTree.AND;
    case OR:
      return JCTree.OR;
    }
    throw new Error("BAD OP");
  }

  private int transAssignOp(Op op) {
    switch (op) {
    case MUL:
      return JCTree.MUL_ASG;
    case DIV:
      return JCTree.DIV_ASG;
    case MOD:
      return JCTree.MOD_ASG;
    case ADD:
      return JCTree.PLUS_ASG;
    case SUB:
      return JCTree.MINUS_ASG;
    }
    throw new Error("BAD ASSIGN OP");
  }

  public JCExpression gen(String meth, JCExpression... es) {
    // TODO: using syms.objectType causes more casts than necessary
    return call(factoryId(), meth, expressionType, es);
  }

  public JCExpression gen(String meth, List<JCExpression> args) {
    // TODO: using syms.objectType causes more casts than necessary
    return call(factoryId(), meth, expressionType, args);
  }

  public JCExpression call(JCExpression target, String meth, Type type,
      JCExpression... es) {
    List<JCExpression> args = List.nil();
    for (JCExpression e : es)
      args = args.append(e);
    return call(target, meth, type, args);
  }

  public JCExpression call(JCExpression target, String meth, Type type,
      List<JCExpression> args) {
    return call(target, target.type, meth, type, args, List.<Type> nil());
  }

  public JCExpression call(JCExpression target, Type targetType, String meth,
      Type resType, List<JCExpression> args, List<Type> typeargs) {
    List<Type> argtypes = List.nil();
    for (JCExpression arg : args)
      argtypes = argtypes.append(arg.type);

    Name name = names.fromString(meth);
    MethodSymbol ms = rs.resolveInternalMethod(
        target == null ? null : target.pos(), attrEnv, targetType, name,
        argtypes, typeargs);
    JCExpression method;
    if (target == null) {
      JCIdent id = make.Ident(ms);
      id.sym = ms;
      method = id;
    } else {
      JCFieldAccess fa = (JCFieldAccess) make.Select(target, ms);
      fa.sym = ms;
      method = fa;
    }
    method.setType(ms.type);
    JCMethodInvocation app = make.App(method, args);
    app.setType(resType);
    if (ms.isVarArgs()) {
      app.varargsElement = syms.objectType; // only case of varargs is E...
    }
    return app;
  }

  public abstract class GeneratorWithArgs extends Generator {
    public abstract java.util.List<Generator> getArgs();
  }

  @Override
  public Generator Data(final Object value) {
    return new Generator() {

      public JCExpression generateExpr() {
        return make.Literal(value);
      }

      public JCStatement generateStmt() {
        return make.Skip();
      }

      public JCExpression generateRemote() {
        if (value instanceof JCExpression) {
          // create a lambda
          // f$.Fun("x", <value>.apply$getRemote(in, f$, f$.Var("x")));
          return gen(
              "Fun",
              make.Literal("x"),
              call((JCExpression) value, "apply$getRemote", expressionType,
                  make.Ident(in), factoryId(), Var("x").generateRemote()));
        } else {
          // create a literal
          // f$.Data(<data>);
          return gen("Data", make.Literal(value));
        }
      }
    };
  }

  @Override
  public Generator If(final Generator condition, final Generator thenExp,
      final Generator elseExp) {
    return new Generator() {
      public JCExpression generateExpr() {
        JCExpression a = condition.generateExpr();
        JCExpression b = thenExp.generateExpr();
        JCExpression c = elseExp.generateExpr();
        return make.Conditional(a, b, c).setType(b.type);
      }

      @Override
      public JCStatement generateStmt() {
        return make.If(condition.generateExpr(), thenExp.generateStmt(),
            elseExp.generateStmt());
      }

      @Override
      public JCExpression generateRemote() {
        return gen("If", condition.generateRemote(), thenExp.generateRemote(),
            elseExp.generateRemote());
      }
    };
  }

  @Override
  public Generator Loop(final String var, final Generator collection,
      final Generator body) {
    return new Generator() {
      public JCExpression generateExpr() {
        throw new Error("TODO: allow aggregation used locally");
      }

      public JCStatement generateStmt() {
        JCExpression items = call(make.Ident(in), "getIteration",
            syms.batchMultiForestType, make.Literal(var));
        VarSymbol subresult = new VarSymbol(0, names.fromString(var),
            syms.batchForestType, currentMethodSym);
        JCVariableDecl decl = make.VarDef(subresult, null);
        decl.setType(syms.batchForestType);
        Symbol save;
        if (out == null) {
          save = in;
          in = subresult;
        } else {
          save = out;
          out = subresult;
        }
        JCStatement sub = body.generateStmt();
        if (out == null) {
          in = save;
        } else {
          out = save;
        }
        return make.ForeachLoop(decl, items, sub);
      }

      public JCExpression generateRemote() {
        return gen("Loop", make.Literal(var), collection.generateRemote(),
            body.generateRemote());
      }
    };
  }

  @Override
  public Generator Call(final Generator target, final String method,
      final java.util.List<Generator> args) {
    return new Generator() {
      public JCExpression generateExpr() {
        List<JCExpression> a2 = List.nil();
        for (Generator e : args)
          a2 = a2.append(e.generateExpr());
        return call(target.generateExpr(), method, (Type) extraInfo, a2);
      }

      public JCStatement generateStmt() {
        return make.Exec(generateExpr());
      }

      public JCExpression generateRemote() {
        List<JCExpression> a2 = List.nil();
        a2 = a2.append(target.generateRemote());
        a2 = a2.append(make.Literal(method));
        for (Generator e : args)
          a2 = a2.append(e.generateRemote());
        return gen("Call", a2);
      }
    };
  }

  // FACTORY
  @Override
  public Generator DynamicCall(final Generator target, final String method,
      final java.util.List<Generator> args) {
    return new Generator() {
      public JCExpression generateExpr() {
        JCDynamicCallInfo info = (JCDynamicCallInfo) extraInfo;
        if (info.returns == Place.REMOTE) {
          return make.Literal(null);
        } else {
          if (target.noTarget()) {
            return call(null, info.meth.owner.type, method + "$getRemote",
                expressionType, List.<JCExpression> of(make.Ident(in)),
                List.<Type> of(expressionType));
          } else
            return call(target.generateExpr(), method + "$postLocal",
                expressionType, make.Ident(in));
        }
      }

      @Override
      public JCStatement generateStmt() {
        return make.Exec(generateExpr());
      }

      // generate the remote part of a dynamic procedure
      @Override
      public JCExpression generateRemote() {
        JCDynamicCallInfo info = (JCDynamicCallInfo) extraInfo;
        List<JCExpression> a2 = List.nil();
        a2 = a2.append(make.Ident(in));
        a2 = a2.append(factoryId());
        int i = 0;
        for (Generator e : args)
          if (info.arguments.get(i++) == Place.REMOTE)
            a2 = a2.append(e.generateRemote());
          else
            a2 = a2.append(e.generateExpr());
        if (target.noTarget()) {
          return call(null, info.meth.owner.type, method + "$getRemote",
              expressionType, a2, List.<Type> of(expressionType));
        } else {
          JCExpression targetExp = target.generateExpr();
          return call(targetExp, targetExp.type, method + "$getRemote",
              expressionType, a2, List.<Type> of(expressionType));
        }
      }
    };
  }

  @Override
  public Generator Mobile(final String type, final Generator expression) {
    return new Generator() {
      public JCExpression generateExpr() {
        return expression.generateExpr();
      }

      @Override
      public JCExpression generateRemote() {
        return expression.generateRemote();
      }

      @Override
      public JCStatement generateStmt() {
        return expression.generateStmt();
      }
    };
  }

  @Override
  public Generator Var(final String name) {
    return new Generator() {
      public JCExpression generateExpr() {
        if (extraInfo instanceof JCVariableDecl)
          return make.Ident(((JCVariableDecl) extraInfo).sym);
        else
          return make.Ident((Symbol) extraInfo);
      }

      public JCStatement generateStmt() {
        return make.Skip();
      }

      @Override
      public JCExpression generateRemote() {
        if (name.equals(root))
          return gen("Root");
        else {
          return gen("Var", make.Literal(name));
        }
      }

      @SuppressWarnings("unused")
      public String varName() {
        return name;
      }
    };
  }

  @Override
  public Generator Prim(final Op op, final java.util.List<Generator> args) {
    return new Generator() {
      @Override
      public JCExpression generateExpr() {
        switch (args.size()) {
        case 0:
          return make.Literal(null);
        case 1:
          JCExpression x = (args.get(0)).generateExpr();
          switch (op) {
          case NOT:
            return make.Unary(JCTree.NOT, x).setType(syms.booleanType);
          }
          break;
        case 2: {
          JCExpression lhs = (args.get(0)).generateExpr();
          JCExpression rhs = (args.get(1)).generateExpr();
          int tag = transOp(op);
          JCBinary opResult = make.Binary(tag, lhs, rhs);
          Symbol newOperator = rs.resolveBinaryOperator(null, tag, attrEnv,
              lhs.type, rhs.type);
          opResult.operator = newOperator;
          opResult.type = newOperator.type.getReturnType();
          return opResult;
        }
        }
        throw new Error("TODO!");
      }

      @Override
      public JCStatement generateStmt() {
        if (args.size() == 0)
          return make.Skip();

        List<JCStatement> stats = List.nil();
        for (Generator e : args)
          stats = stats.append(e.generateStmt());
        return make.Block(0, stats);
      }

      @Override
      public JCExpression generateRemote() {
        List<JCExpression> a2 = List.nil();
        a2 = a2.append(makeOpLiteral(op));
        int count = 0;
        Generator preCond = null;
        JCExpression result = null;
        for (Generator e : args)
          if (false // TODO!!! e.is(batch.syntax.Base.Kind.In)
          && (op == Op.AND || op == Op.OR)) {
            preCond = batch.syntax.Factory.binary(factory, op, preCond, e);
          } else {
            result = e.generateRemote();
            a2 = a2.append(result);
            count++;
          }
        if (count != 1 || op.unary())
          result = gen("Prim", a2);
        /* TODO: Partial evaluation optimization!!
        if (preCond != null) {
          MethodAccess callConst = new MethodAccess();
          call.setID("WHAT");
          callConst.addArg(make.Literal(op == Op.OR));
          JCExpression constVal = new Dot(new VarAccess("f$"), callConst);
          JCExpression preCondJ = preCond.generateExpr(in, null);
          if (op == Op.AND)
            result = make.Conditional(preCondJ, result, constVal);
          else
            result = make.Conditional(preCondJ, constVal, result);
        }
        */
        return result;
      }
    };
  }

  // jastadd specific
  @Override
  public Generator Let(final String name, final Generator expression,
      final Generator body) {
    return new Generator() {
      public JCExpression generateExpr() {
        throw new Error("Let used as exp");
      }

      public JCStatement generateStmt() {
        List<JCStatement> stats = List.nil();
        JCVariableDecl decl = (JCVariableDecl) extraInfo;
        JCVariableDecl cmd = make.VarDef(decl.getModifiers(), decl.getName(),
            (JCExpression) decl.getType(), expression.generateExpr());
        cmd.setType(decl.type);
        cmd.sym = decl.sym;
        stats = stats.append(cmd);
        stats = stats.append(body.generateStmt());
        return make.Block(0, stats);
      }

      public JCExpression generateRemote() {
        return gen("Let", make.Literal(name), expression.generateRemote(),
            body.generateRemote());
      }
    };
  }

  @Override
  public Generator Out(final String location, final Generator expression) {
    return new Generator() {
      public JCExpression generateExpr() {
        JCExpression result = expression.generateExpr();
        return call(make.Ident(out), "put", syms.voidType,
            make.Literal(location), result);
      }

      @Override
      public JCStatement generateStmt() {
        return make.Exec(generateExpr());
      }

      @Override
      public JCExpression generateRemote() {
        return gen("Out", make.Literal(location), expression.generateRemote());
      }
    };
  }

  @Override
  public Generator In(final String location) {
    return new Generator() {
      public JCExpression generateExpr() {
        // java.util.Date and java.sql.Date have same short name
        // Feature: ENUM turned off for now
        // if (type.indexOf('.') > 0) {
        // call.setID("getEnum");
        // args.add(new Dot(new TypeAccess(type),
        // new ClassAccess()));
        // } else
        String typeName = (String) extraInfo;
        Type type = batchToJavaType(typeName);
        JCExpression lit = make.Literal(location);
        return call(make.Ident(in), "get" + typeName, type, lit).setType(type);
      }

      private Type batchToJavaType(String typeName) throws Error {
        Type type;
        if (typeName.equals("String"))
          type = syms.stringType;
        else if (typeName.equals("Integer"))
          type = syms.intType;
        else if (typeName.equals("Float"))
          type = syms.floatType;
        else if (typeName.equals("Boolean"))
          type = syms.booleanType;
        else if (typeName.equals("Character"))
          type = syms.charType;
        else if (typeName.equals("RawData"))
          type = new Type.ArrayType(syms.byteType, syms.arrayClass);
        else if (typeName.equals("Date"))
          type = syms.dateType;
        else if (typeName.equals("Time"))
          type = syms.timeType;
        else
          throw new Error("Unkonwn Type");
        return type;
      }

      @Override
      public JCStatement generateStmt() {
        throw new Error("input used as statement");
      }

      @Override
      public JCExpression generateRemote() {
        return gen("In", make.Literal(location));
      }
    };
  }

  // FACTORY

  @Override
  public Generator Prop(final Generator base, final String field) {
    return new Generator() {
      public JCExpression generateExpr() {
        Type baseType = base.extraInfo instanceof Type ? (Type) base.extraInfo
            : ((Symbol) base.extraInfo).type;
        Symbol sym = rs.resolveInternalField(null, attrEnv, baseType,
            names.fromString(field));
        return make.Select(base.generateExpr(), sym).setType((Type) extraInfo);
      }

      @Override
      public JCStatement generateStmt() {
        return make.Skip();
      }

      @Override
      public JCExpression generateRemote() {
        return gen("Prop", base.generateRemote(), make.Literal(field));
      }
    };
  }

  // FACTORY

  @Override
  public Generator Assign(final Generator target, final Generator source) {
    return new Generator() {
      public JCExpression generateExpr() {
        JCExpression lhs = target.generateExpr();
        JCExpression rhs = source.generateExpr();
        return make.Assign(lhs, rhs).setType(rhs.type);
      }

      @Override
      public JCStatement generateStmt() {
        return make.Exec(generateExpr());
      }

      @Override
      public JCExpression generateRemote() {
        return gen("Assign", target.generateRemote(), source.generateRemote());
      }
    };
  }

  @Override
  public Generator Other(final Object obj, final java.util.List<Generator> args) {
    return new GeneratorWithArgs() {
      public boolean noTarget() {
        return obj == null;
      }

      public JCExpression generateExpr() {
        JCTree part = spliceArgs((JCTree) obj, args);
        return (JCExpression) part;
      }

      @Override
      public JCStatement generateStmt() {
        JCTree part = spliceArgs((JCTree) obj, args);
        if (obj instanceof JCStatement)
          return (JCStatement) part;
        else
          return make.Exec((JCExpression) part);
      }

      @Override
      public JCExpression generateRemote() {
        throw new Error("unknown code used remotely");
      }

      public java.util.List<Generator> getArgs() {
        return args;
      }
    };
  }

  protected JCTree spliceArgs(JCTree obj, java.util.List<Generator> args) {
    return obj.accept(conv, args);
  }

  @Override
  public Generator Fun(String var, Generator body) {
    throw new Error("NOT IMPLEMENTED");
  }

}
