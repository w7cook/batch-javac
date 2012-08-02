package org.civet;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Name;

public class PartialObject extends HPEObject {

  private JCClassDecl classDecl;

  HPEFrame frame = new HPEFrame();

  public PartialObject() {
    kind = Kind.PARTIAL;
  }

  public JCClassDecl getClassDecl() {
    return classDecl;
  }

  @Override
  public HPEObject getFieldValue(Name n) {
    return frame.lookup(HPESymbol.field(n, this));
  }

  @Override
  public boolean hasField(Name n) {
    return frame.contains(HPESymbol.field(n, this));
  }

  public void setClassDecl(JCClassDecl classDecl) {
    this.classDecl = classDecl;
  }

  @Override
  public void setFieldValue(Name n, HPEObject v) {
    frame.add(HPESymbol.field(n, this), v.convertToField(n, this));
  }

  @Override
  public String toString() {
    return frame.toString();
  }

}
