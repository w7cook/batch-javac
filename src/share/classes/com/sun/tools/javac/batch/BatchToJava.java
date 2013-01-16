package com.sun.tools.javac.batch;

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
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
import com.sun.tools.javac.tree.JCTree.JCAssert;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCBreak;
import com.sun.tools.javac.tree.JCTree.JCCase;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCInstanceOf;
import com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCSwitch;
import com.sun.tools.javac.tree.JCTree.JCSynchronized;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTry;
import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

/* copies the sub-expressions in an Other node back
 * into the original Java Compiler AST nodes.
 */
public class BatchToJava implements
    TreeVisitor<JCTree, java.util.List<Generator>> {

  public BatchToJava(Context context) {
  }

  JCExpression exp(Generator g) {
    return g.generateExpr();
  }

  JCStatement stat(Generator g) {
    return g.generateStmt();
  }

  JCBlock block(Generator g) {
    return (JCBlock) g.generateStmt();
  }

  @Override
  public JCTree visitAnnotation(AnnotationTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitMethodInvocation(MethodInvocationTree node,
      java.util.List<Generator> info) {
    JCMethodInvocation call = (JCMethodInvocation) node;
    call.meth = exp(info.get(0));
    List<JCExpression> args = call.args;
    for (int i = 1; i < info.size(); i++) {
      args.head = exp(info.get(i));
      args = args.tail;
    }
    return call;
  }

  private List<JCExpression> toExpList(java.util.List<Generator> info, int start) {
    List<JCExpression> args = List.<JCExpression> nil();
    for (int i = start; i < info.size(); i++)
      args = args.append(exp(info.get(i)));
    return args;
  }

  @Override
  public JCTree visitAssert(AssertTree node, java.util.List<Generator> info) {
    JCAssert ass = (JCAssert)node;
    ass.cond = exp(info.get(0));
    ass.detail = exp(info.get(1));
    return ass;
  }

  @Override
  public JCTree visitAssignment(AssignmentTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitCompoundAssignment(CompoundAssignmentTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitBinary(BinaryTree node, java.util.List<Generator> info) {
    JCBinary bin = (JCBinary) node;
    bin.lhs = exp(info.get(0));
    bin.rhs = exp(info.get(1));
    return bin;
  }

  @Override
  public JCTree visitBlock(BlockTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitBreak(BreakTree node, java.util.List<Generator> info) {
    JCBreak br = (JCBreak)node;
    return br;
  }

  @Override
  public JCTree visitCase(CaseTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitCatch(CatchTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitClass(ClassTree node, java.util.List<Generator> info) {
    return (JCTree) node;
  }

  @Override
  public JCTree visitConditionalExpression(ConditionalExpressionTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitContinue(ContinueTree node, java.util.List<Generator> info) {
    return (JCTree) node;
  }

  @Override
  public JCTree visitDoWhileLoop(DoWhileLoopTree node,
      java.util.List<Generator> info) {
    JCWhileLoop w = (JCWhileLoop)node;
    w.cond = exp(info.get(0));
    w.body = stat(info.get(1));
    return w;
  }

  @Override
  public JCTree visitErroneous(ErroneousTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitExpressionStatement(ExpressionStatementTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitEnhancedForLoop(EnhancedForLoopTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitForLoop(ForLoopTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
    /*    Tree[] args = new Tree[2 + node.getInitializer().size()
            + node.getUpdate().size()];
        int i = 0;
        for (Tree x : node.getInitializer())
          args[i++] = x;
        args[i++] = node.getCondition();
        for (Tree x : node.getUpdate())
          args[i++] = x;
        args[i++] = node.getStatement();
        return other(node, args);*/
  }

  @Override
  public JCTree visitIdentifier(IdentifierTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitIf(IfTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitImport(ImportTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitArrayAccess(ArrayAccessTree node,
      java.util.List<Generator> info) {
    JCArrayAccess aa = (JCArrayAccess)node;
    aa.indexed = exp(info.get(0));
    aa.index = exp(info.get(1));
    return aa;
  }

  @Override
  public JCTree visitLabeledStatement(LabeledStatementTree node,
      java.util.List<Generator> info) {
    JCLabeledStatement ls = (JCLabeledStatement)node;
    ls.body = stat(info.get(0));
    return ls;
  }

  @Override
  public JCTree visitLiteral(LiteralTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitMethod(MethodTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitModifiers(ModifiersTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitNewArray(NewArrayTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitNewClass(NewClassTree node, java.util.List<Generator> info) {
    JCNewClass nc = (JCNewClass) node;
    nc.args = toExpList(info, 0);
    return nc;
  }

  @Override
  public JCTree visitParenthesized(ParenthesizedTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitReturn(ReturnTree node, java.util.List<Generator> info) {
    JCReturn r = (JCReturn)node;
    r.expr = exp(info.get(0));
    return r;
  }

  @Override
  public JCTree visitMemberSelect(MemberSelectTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitEmptyStatement(EmptyStatementTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitSwitch(SwitchTree node, java.util.List<Generator> info) {
    JCSwitch sw = (JCSwitch)node;
    sw.selector = exp(info.get(0));    
    int i = 1;
    for (CaseTree x : node.getCases()) {
      JCCase c = (JCCase)x;
      c.pat = exp(info.get(i++));
      c.stats = List.of(stat(info.get(i++))); // NOTE: can wrap a block around some statements
    }
    return sw;
  }

  @Override
  public JCTree visitSynchronized(SynchronizedTree node,
      java.util.List<Generator> info) {
    JCSynchronized s = (JCSynchronized)node;
    s.lock = exp(info.get(0));
    s.body = block(info.get(1));
    return s;
  }

  @Override
  public JCTree visitThrow(ThrowTree node, java.util.List<Generator> info) {
    JCThrow s = (JCThrow)node;
    s.expr = exp(info.get(0));
    return s;
  }

  @Override
  public JCTree visitCompilationUnit(CompilationUnitTree node,
      java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitTry(TryTree node, java.util.List<Generator> info) {
    JCTry t = (JCTry)node;
    t.body = block(info.get(0));
    t.finalizer = block(info.get(1));
    int i = 2;
    for (CatchTree x : node.getCatches()) {
      JCCatch c = (JCCatch)x;
      c.body = block(info.get(i++));
    }
    return t;
  }

  @Override
  public JCTree visitParameterizedType(ParameterizedTypeTree node,
      java.util.List<Generator> info) {
    return (JCTree) node;
  }

  @Override
  public JCTree visitUnionType(UnionTypeTree node,
      java.util.List<Generator> info) {
    return (JCTree) node;
  }

  @Override
  public JCTree visitArrayType(ArrayTypeTree node,
      java.util.List<Generator> info) {
    return (JCTree) node;
  }

  @Override
  public JCTree visitTypeCast(TypeCastTree node, java.util.List<Generator> info) {
    JCTypeCast tc = (JCTypeCast)node;
    tc.expr = exp(info.get(0));
    return tc;
  }

  @Override
  public JCTree visitPrimitiveType(PrimitiveTypeTree node,
      java.util.List<Generator> info) {
    return (JCTree) node;
  }

  @Override
  public JCTree visitTypeParameter(TypeParameterTree node,
      java.util.List<Generator> info) {
    return (JCTree) node;
  }

  @Override
  public JCTree visitInstanceOf(InstanceOfTree node,
      java.util.List<Generator> info) {
    JCInstanceOf inst = (JCInstanceOf)node;
    inst.expr = exp(info.get(0));
    return inst;
  }

  @Override
  public JCTree visitUnary(UnaryTree node, java.util.List<Generator> info) {
    JCUnary un = (JCUnary) node;
    un.arg = exp(info.get(0));
    return un;
  }

  @Override
  public JCTree visitVariable(VariableTree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

  @Override
  public JCTree visitWhileLoop(WhileLoopTree node,
      java.util.List<Generator> info) {
    JCWhileLoop loop = (JCWhileLoop)node;
    loop.cond = exp(info.get(0));
    loop.body = stat(info.get(1));
    return loop;
  }

  @Override
  public JCTree visitWildcard(WildcardTree node, java.util.List<Generator> info) {
    return (JCTree) node;
  }

  @Override
  public JCTree visitOther(Tree node, java.util.List<Generator> info) {
    throw new Error("MISSING");
  }

}
