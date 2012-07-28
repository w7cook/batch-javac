package org.civet;

import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.List;

public class Method {
  private MethodSymbol methodSymbol;
  private MethodSymbol genMethodSymbol;
  private List<Parameter> ctParams = List.<Parameter> nil();
  private String genMethodName;
  private HPEContext hpeContext;
  private JCClassDecl specializedClass;
  
  
  public JCClassDecl getSpecializedClass() {
    return specializedClass;
  }

  public void setSpecializedClass(JCClassDecl specializedClass) {
    this.specializedClass = specializedClass;
  }

  public void appendParam(int i, HPEObject arg) {
    ctParams.append(new Parameter(i, arg));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((methodSymbol == null) ? 0 : methodSymbol.hashCode());
    result = prime * result
        + ((genMethodName == null) ? 0 : genMethodName.hashCode());
    result = prime * result + ((ctParams == null) ? 0 : ctParams.hashCode());
    return result;
  }

  public Method(MethodSymbol sym, List<Parameter> params, HPEContext hpeContext) {
    this.methodSymbol = sym;
    this.ctParams = params;
    this.hpeContext = hpeContext;
  }

  private boolean equalsParams(List<Parameter> l1, List<Parameter> l2) {
    if (l1.size() == l2.size()) {
      for (int i = 0; i < l1.size(); i++) {
        if (!(l1.get(i).equals(l2.get(i))))
          return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Method)) {
      return false;
    }
    Method other = (Method) obj;
    if (methodSymbol == null) {
      if (other.methodSymbol != null) {
        return false;
      }
    } else if (!hpeContext.equalsMethodSym(methodSymbol, other.methodSymbol)) {
      return false;
    }
    if (ctParams == null) {
      if (other.ctParams != null) {
        return false;
      }
    } else if (!equalsParams(ctParams, other.ctParams)) {
      return false;
    }

    return true;
  }

  public String getGenMethodName() {
    return genMethodName;
  }

  public Method setGenMethodName(String genMethodName) {
    this.genMethodName = genMethodName;
    return this;
  }

  public MethodSymbol getGenMethodSymbol() {
    return genMethodSymbol;
  }

  public void setGenMethodSymbol(MethodSymbol genMethodSymbol) {
    this.genMethodSymbol = genMethodSymbol;
  }

}
