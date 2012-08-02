package org.civet;

public abstract class Primitive extends HPEObject {
  public Primitive(Object v) {
    object = v;
  }

  @Override
  public HPEObject clone() throws CloneNotSupportedException {
    return this; // FIXME: make a real clone
  }

  @Override
  public String toString() {
    return object.toString();
  }
  
  public static class NUMBER extends Primitive {
    public static enum NumType {
      INT, LONG, DOUBLE, FLOAT
    }

    NumType numType;

    private NUMBER(Number v, NumType nt) {
      super(v);
      kind = Kind.NUMBER;
      numType = nt;
    }

    public static NUMBER INT(Integer o) {
      return new NUMBER(o, NumType.INT);
    }

    public static NUMBER LONG(Long o) {
      return new NUMBER(o, NumType.LONG);
    }

    public static NUMBER FLOAT(Float o) {
      return new NUMBER(o, NumType.FLOAT);
    }

    public static NUMBER DOUBLE(Double o) {
      return new NUMBER(o, NumType.DOUBLE);
    }
  }

  public static class BOOLEAN extends Primitive {
    public BOOLEAN(Boolean v) {
      super(v);
      kind = Kind.BOOLEAN;
    }

    public BOOLEAN(String s) {
      this(Boolean.valueOf(s));
    }
  }

  public static class STRING extends Primitive {
    public STRING(String v) {
      super(v);
      kind = Kind.STRING;
    }
  }

  public static class CHAR extends Primitive {
    public CHAR(Character v) {
      super(v);
      kind = Kind.CHARACTER;
    }
  }

  public static class NULL extends Primitive {
    public NULL() {
      super(null);
      kind = Kind.NULL;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (!(obj instanceof NULL))
        return false;
      return true;
    }
  }


}