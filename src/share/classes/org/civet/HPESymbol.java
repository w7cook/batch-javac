package org.civet;

import com.sun.tools.javac.util.Name;

public class HPESymbol {

  Boolean isField;
  Boolean isArrayItem;
  Integer arrayIndex;
  Name name;
  HPEObject owner;
  
  

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("");
    if (isField) sb.append(owner.name).append(".");
    if (isArrayItem) 
      sb.append(owner.name).append("[").append(arrayIndex).append("]");
    else sb.append(name);

    return sb.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((arrayIndex == null) ? 0 : arrayIndex.hashCode());
    result = prime * result + ((isArrayItem == null) ? 0 : isArrayItem.hashCode());
    result = prime * result + ((isField == null) ? 0 : isField.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((owner == null) ? 0 : owner.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HPESymbol other = (HPESymbol) obj;
    if (arrayIndex == null) {
      if (other.arrayIndex != null)
        return false;
    } else if (!arrayIndex.equals(other.arrayIndex))
      return false;
    if (isArrayItem == null) {
      if (other.isArrayItem != null)
        return false;
    } else if (!isArrayItem.equals(other.isArrayItem))
      return false;
    if (isField == null) {
      if (other.isField != null)
        return false;
    } else if (!isField.equals(other.isField))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (owner == null) {
      if (other.owner != null)
        return false;
    } else if (!owner.equals(other.owner))
      return false;
    return true;
  }

  private HPESymbol(Boolean isField, Name name, HPEObject owner, 
      Boolean isArray, Integer arrayIndx) {
    super();
    this.isField = isField;
    this.name = name;
    this.owner = owner;
    this.isArrayItem = isArray;
    this.arrayIndex = arrayIndx;
  }

  public static HPESymbol local(Name name) {
    return new HPESymbol(false, name, null, false, -1);
  }
  
  public static HPESymbol field(Name name, HPEObject owner) {
    return new HPESymbol(true, name, owner, false, -1);
  }
  
  public static HPESymbol arrayItem(HPEObject array, Integer indx) {
    return new HPESymbol(false, null, array, true, indx);
  }
  
  public static HPESymbol fromHPEObject(HPEObject v) {
    if (v.isField) {
      return HPESymbol.field(v.name, v.ownerObject);
    }
    return HPESymbol.local(v.name);
  }
  
  public void set(HPEObject v) {
    if (isField) {
      if (owner.isConcrete())
        owner.setFieldValue(this.name, v);
    }
    else if (isArrayItem) {
      ObjectLiteral ol = (ObjectLiteral) owner;
      assert(ol.isArray);
      ol.setArrayItem(this.arrayIndex, v);
    }
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {

  }

}
