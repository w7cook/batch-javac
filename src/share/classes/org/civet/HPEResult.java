package org.civet;

import com.sun.source.tree.Tree;

public class HPEResult {
  Tree code;
  private HPEObject value;

  public HPEResult(Tree code, HPEObject value) {
    this.code = code;
    if (value.isPrimitive()) {
      this.value = HPEObject.fromObject(value.object).setTag(value.tag);      
    }
    else {
      this.value = value;
    }
    this.value.symbol = value.symbol;
  }
  
  public HPEObject value() {
    return value;
  }

  
  public Tree getCode() {
    return code;
  }

  public void setValue(HPEObject value) {
    this.value = value;
  }  
}
