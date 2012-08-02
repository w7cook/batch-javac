package org.civet;

import com.sun.source.tree.TreeVisitor;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;

public class JCSkipExpr extends JCExpression {

  private JCSkipExpr() {
    
  }
  
  private static JCSkipExpr instance = new JCSkipExpr();
  
  public static JCSkipExpr instance() {
    return instance;
  }
  
  @Override
  public Kind getKind() {
    return Kind.EMPTY_STATEMENT;
  }

  @Override
  public int getTag() {
    return JCTree.SKIP;
  }

  @Override
  public void accept(Visitor v) {

  }

  @Override
  public <R, D> R accept(TreeVisitor<R, D> v, D d) {
    return null;
  }

}
