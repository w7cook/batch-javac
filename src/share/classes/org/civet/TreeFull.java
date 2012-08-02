package org.civet;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
import com.sun.source.tree.ExpressionTree;
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
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.util.List;

public class TreeFull extends TreeScanner<HPEObject, HPEContext> {

  @Override
  public HPEObject visitCompilationUnit(CompilationUnitTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitCompilationUnit(node, p);
  }

  @Override
  public HPEObject visitImport(ImportTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitImport(node, p);
  }

  @Override
  public HPEObject visitClass(ClassTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitClass(node, p);
  }

  @Override
  public HPEObject visitMethod(MethodTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitMethod(node, p);
  }

  @Override
  public HPEObject visitVariable(VariableTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitVariable(node, p);
  }

  @Override
  public HPEObject visitEmptyStatement(EmptyStatementTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitEmptyStatement(node, p);
  }

  @Override
  public HPEObject visitBlock(BlockTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitBlock(node, p);
  }

  @Override
  public HPEObject visitDoWhileLoop(DoWhileLoopTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitDoWhileLoop(node, p);
  }

  @Override
  public HPEObject visitWhileLoop(WhileLoopTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitWhileLoop(node, p);
  }

  @Override
  public HPEObject visitForLoop(ForLoopTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitForLoop(node, p);
  }

  @Override
  public HPEObject visitEnhancedForLoop(EnhancedForLoopTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitEnhancedForLoop(node, p);
  }

  @Override
  public HPEObject visitLabeledStatement(LabeledStatementTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitLabeledStatement(node, p);
  }

  @Override
  public HPEObject visitSwitch(SwitchTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitSwitch(node, p);
  }

  @Override
  public HPEObject visitCase(CaseTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitCase(node, p);
  }

  @Override
  public HPEObject visitSynchronized(SynchronizedTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitSynchronized(node, p);
  }

  @Override
  public HPEObject visitTry(TryTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitTry(node, p);
  }

  @Override
  public HPEObject visitCatch(CatchTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitCatch(node, p);
  }

  @Override
  public HPEObject visitConditionalExpression(ConditionalExpressionTree node,
      HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitConditionalExpression(node, p);
  }

  @Override
  public HPEObject visitIf(IfTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitIf(node, p);
  }

  @Override
  public HPEObject visitExpressionStatement(ExpressionStatementTree node,
      HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitExpressionStatement(node, p);
  }

  @Override
  public HPEObject visitBreak(BreakTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitBreak(node, p);
  }

  @Override
  public HPEObject visitContinue(ContinueTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitContinue(node, p);
  }

  @Override
  public HPEObject visitReturn(ReturnTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitReturn(node, p);
  }

  @Override
  public HPEObject visitThrow(ThrowTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitThrow(node, p);
  }

  @Override
  public HPEObject visitAssert(AssertTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitAssert(node, p);
  }

  @Override
  public HPEObject visitMethodInvocation(MethodInvocationTree node, HPEContext p) {
    CLOSURE methodSelect = (CLOSURE) node.getMethodSelect().accept(this, p);
    assert (methodSelect.kind == HPEObject.Kind.CLOSURE);
    List<HPEObject> args = List.<HPEObject> nil();
    for (ExpressionTree a : node.getArguments()) {
      args = args.append(a.accept(this, p));
    }
    return methodSelect.invoke(args);
  }

  @Override
  public HPEObject visitNewClass(NewClassTree node, HPEContext p) {
    CLASS classIdent = (CLASS) node.getIdentifier().accept(this, p);
    List<HPEObject> args = List.<HPEObject> nil();
    for (ExpressionTree a : node.getArguments()) {
      args = args.append(a.accept(this, p));
    }
    return classIdent.newInstance(((JCNewClass) node).constructorType, args);
  }

  @Override
  public HPEObject visitNewArray(NewArrayTree node, final HPEContext p) {
    JCNewArray newArray = (JCNewArray) node;
    CLASS arrayType = (CLASS) newArray.getType().accept(this, p);
    List<HPEObject> dims = Function.map(
        new Function<HPEObject, JCExpression>() {
          @Override
          public HPEObject apply(JCExpression t) {
            return t.accept(TreeFull.this, p);
          }
        }, newArray.getDimensions());
    List<HPEObject> inits = Function.map(
        new Function<HPEObject, JCExpression>() {
          @Override
          public HPEObject apply(JCExpression t) {
            return t.accept(TreeFull.this, p);
          }
        }, newArray.getInitializers());

    Object objectArray = null;
    if (dims.length() > 0) {
      try {
        int[] darray = new int[dims.length()];
        int i = 0;
        for (HPEObject d : dims) {
          darray[i++] = Integer.valueOf(d.value().toString());
        }
        objectArray = java.lang.reflect.Array.newInstance(
            (Class<?>) arrayType.value(), darray);
        ObjectLiteral arrayObjectLiteral = new ObjectLiteral(objectArray, true);
        return arrayObjectLiteral;
      } catch (NegativeArraySizeException e) {
        throw new HPEException(e);
      }
    } else {
      try {
        objectArray = java.lang.reflect.Array.newInstance(
            (Class<?>) arrayType.value(), inits.length());
        for (int j = 0; j < inits.length(); j++) {
          java.lang.reflect.Array.set(objectArray, j, inits.get(j).value());
        }
        ObjectLiteral arrayObjectLiteral = new ObjectLiteral(objectArray, true);
        return arrayObjectLiteral;
      } catch (NegativeArraySizeException e) {
        throw new HPEException(e);
      }
    }
  }

  @Override
  public HPEObject visitParenthesized(ParenthesizedTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitParenthesized(node, p);
  }

  @Override
  public HPEObject visitAssignment(AssignmentTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitAssignment(node, p);
  }

  @Override
  public HPEObject visitCompoundAssignment(CompoundAssignmentTree node,
      HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitCompoundAssignment(node, p);
  }

  @Override
  public HPEObject visitUnary(UnaryTree node, HPEContext p) {
    JCUnary unary = (JCUnary) node;
    HPEObject argv = unary.arg.accept(this, p);
    return HPEUtil.unaryOp(unary, argv, p);
  }

  @Override
  public HPEObject visitBinary(BinaryTree node, HPEContext p) {
    JCBinary binary = (JCBinary) node;
    HPEObject lo = node.getLeftOperand().accept(this, p);
    HPEObject ro = node.getRightOperand().accept(this, p);
    HPEObject r = HPEUtil.binOp(binary.getKind(), lo, ro);
    return r;
  }

  @Override
  public HPEObject visitTypeCast(TypeCastTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitTypeCast(node, p);
  }

  @Override
  public HPEObject visitInstanceOf(InstanceOfTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitInstanceOf(node, p);
  }

  @Override
  public HPEObject visitArrayAccess(ArrayAccessTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitArrayAccess(node, p);
  }

  @Override
  public HPEObject visitMemberSelect(MemberSelectTree node, HPEContext p) {
    JCFieldAccess fieldAccess = (JCFieldAccess) node;
    HPEObject receiver = fieldAccess.selected.accept(this, p);
    switch (fieldAccess.sym.getKind()) {
      case METHOD:
        return new CLOSURE(receiver.setTag(HPEObject.Tag.CONCRETE),
            (MethodSymbol) fieldAccess.sym);
      case FIELD:
        throw new NotImplementedException();
      default:
        throw new HPEException("Invalid state!");
    }
  }

  @Override
  public HPEObject visitIdentifier(IdentifierTree node, HPEContext p) {
    JCIdent ident = (JCIdent) node;
    switch (ident.sym.getKind()) {
      case CLASS:
        return new CLASS((ClassSymbol) ident.sym);
      case PARAMETER:
      case LOCAL_VARIABLE:
        HPESymbol sym = HPESymbol.local(ident.sym.name);
        HPEObject retp = p.env.lookup(sym);
        if (!retp.isConcrete()) {
          throw new HPEException(sym.name.toString()
              + " must be a compile-time value.");
        }
        retp.symbol = sym;
        return retp;
      case METHOD:
        return new CLOSURE(p.env.getCurrentThis(), (MethodSymbol) ident.sym);
      case FIELD:
        return p.env.getCurrentThis().getFieldValue(ident.sym.name);
      default:
        throw new NotImplementedException();
    }
  }

  @Override
  public HPEObject visitLiteral(LiteralTree node, HPEContext p)
      throws HPEException {
    JCLiteral jcLiteral = (JCLiteral) node;
    switch (jcLiteral.getKind()) {
      case INT_LITERAL:
        return Primitive.NUMBER.INT(new Integer(node.getValue().toString()));
      case DOUBLE_LITERAL:
        return Primitive.NUMBER.DOUBLE(new Double(node.getValue().toString()));
      case LONG_LITERAL:
        return Primitive.NUMBER.LONG(new Long(node.getValue().toString()));
      case FLOAT_LITERAL:
        return Primitive.NUMBER.FLOAT(new Float(node.getValue().toString()));
      case STRING_LITERAL:
        return new Primitive.STRING(new String(node.getValue().toString()));
      case CHAR_LITERAL:
        return new Primitive.CHAR(
            Character.valueOf((Character) node.getValue()));
      case NULL_LITERAL:
        return new Primitive.NULL();
      case BOOLEAN_LITERAL:
        return new Primitive.BOOLEAN(new Boolean(node.getValue().toString()));
      default:
        assert (false);
        break;
    }
    throw new HPEException("Should not be here");
  }

  @Override
  public HPEObject visitPrimitiveType(PrimitiveTypeTree node, HPEContext p) {
    JCPrimitiveTypeTree primitiveTypeTree = (JCPrimitiveTypeTree) node;
    return new CLASS((ClassSymbol) primitiveTypeTree.type.tsym);
  }

  @Override
  public HPEObject visitArrayType(ArrayTypeTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitArrayType(node, p);
  }

  @Override
  public HPEObject visitParameterizedType(ParameterizedTypeTree node,
      HPEContext p) {
    return node.getType().accept(this, p);
  }

  @Override
  public HPEObject visitUnionType(UnionTypeTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitUnionType(node, p);
  }

  @Override
  public HPEObject visitTypeParameter(TypeParameterTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitTypeParameter(node, p);
  }

  @Override
  public HPEObject visitWildcard(WildcardTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitWildcard(node, p);
  }

  @Override
  public HPEObject visitModifiers(ModifiersTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitModifiers(node, p);
  }

  @Override
  public HPEObject visitAnnotation(AnnotationTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitAnnotation(node, p);
  }

  @Override
  public HPEObject visitOther(Tree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitOther(node, p);
  }

  @Override
  public HPEObject visitErroneous(ErroneousTree node, HPEContext p) {
    // TODO Auto-generated method stub
    return super.visitErroneous(node, p);
  }

}
