package org.civet;

public class Parameter {
  int position = -1;
  HPEObject value = null;

  public Parameter(int position, HPEObject value) {
    assert (value.isConcrete() || (value.isAbstract() && value.isPrimitive()));
    this.position = position;
    this.value = value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + position;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Parameter)) {
      return false;
    }
    Parameter other = (Parameter) obj;
    if (position != other.position) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }
}
