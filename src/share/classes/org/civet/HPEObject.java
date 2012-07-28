package org.civet;


import com.sun.tools.javac.util.Name;

public abstract class HPEObject {
  public static enum Kind {
    NUMBER, STRING, PARTIAL, NULL, OBJECT, CHARACTER, BOOLEAN, CLOSURE, CLASS
  }

  public static enum Tag {
    TOP, BOTTOM, ABSTRACT, CONCRETE
  }

  public HPEObject(Tag tag) {
    this.tag = tag;
  }

  public HPEObject() {
  }
  HPESymbol symbol = null;

  Tag tag;
  Kind kind;
  protected Object object = null;

  Object value() {
    return object;
  }

  public HPEObject setTag(Tag tag) {
    this.tag = tag;
    return this;
  }

  Boolean isArray = false;
  Boolean isEnum = false;
  Boolean isField = false;
  Name name;
  HPEObject ownerObject;

  public HPEObject convertToField(Name fieldName, HPEObject owner) {
    this.isField = true;
    this.name = fieldName;
    this.ownerObject = owner;
    return this;
  }

  public boolean isPrimitive() {
    return kind == Kind.NUMBER || kind == Kind.STRING || kind == Kind.NULL
        || kind == Kind.CHARACTER || kind == Kind.BOOLEAN;
  }

  public boolean isConcrete() {
    return tag == Tag.CONCRETE;
  }

  public boolean isAbstract() {
    return tag == Tag.ABSTRACT;
  }

  public boolean isTop() {
    return tag == Tag.TOP;
  }

  public boolean isBottom() {
    return tag == Tag.BOTTOM;
  }

  static java.lang.reflect.Field hasField(java.lang.Class<?> c, String fieldName) {
    try {
      java.lang.reflect.Field f = c.getDeclaredField(fieldName);
      return f;
    } catch (NoSuchFieldException e) {
      java.lang.Class<?> sc = c.getSuperclass();
      if (sc != null)
        return hasField(sc, fieldName);
    } catch (SecurityException e) {
      throw new HPEException(e);
    }
    return null;
  }

  public HPEObject getFieldValue(Name n) {
    throw new UnsupportedOperationException();
  }

  public boolean hasField(Name n) {
    throw new UnsupportedOperationException();
  }

  public void setFieldValue(Name n, HPEObject exp) {
    throw new UnsupportedOperationException();
  }

  @Override
  public HPEObject clone() throws CloneNotSupportedException {
    throw new UnsupportedOperationException();
  }

  @Override
  public abstract String toString();

  public static HPEObject fromObject(Object o) {
    if (o == null)
      return new Primitive.NULL();
    Class<?> clazz = o.getClass();
    if (clazz.equals(String.class))
      return new Primitive.STRING((String) o);
    if (clazz.equals(Integer.class))
      return Primitive.NUMBER.INT((Integer) o);
    if (clazz.equals(Double.class))
      return Primitive.NUMBER.DOUBLE((Double) o);
    if (clazz.equals(Float.class))
      return Primitive.NUMBER.FLOAT((Float) o);
    if (clazz.equals(Character.class))
      return new Primitive.CHAR((Character) o);
    if (clazz.equals(Long.class))
      return Primitive.NUMBER.LONG((Long) o);
    if (clazz.equals(Short.class))
      return Primitive.NUMBER.INT((Integer) o);
    if (clazz.equals(Byte.class))
      return Primitive.NUMBER.INT((Integer) o);
    if (clazz.equals(Boolean.class))
      return new Primitive.BOOLEAN((Boolean) o);
    return new ObjectLiteral(o, clazz.isArray());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof HPEObject))
      return false;
    HPEObject other = (HPEObject) obj;
    if (this.kind != other.kind)
      return false;
    if (this.tag != other.tag)
      return false;
    if (object == null && other.object != null)
      return false;
    return object.equals(other.object);
  }

  public static final HPEObject Top = new HPEObject(Tag.TOP) {

    @Override
    public HPEObject clone() throws CloneNotSupportedException {
      return this;
    }

    @Override
    public HPEObject getFieldValue(Name v) {
      return this;
    }

    @Override
    public String toString() {
      return "T";
    }

  };

  public static final class Bottom extends HPEObject {

    HPEObject data = BOTTOM_OBJECT;

    
    public Bottom() {
      super();
      tag = Tag.BOTTOM;
    }

    @Override
    public HPEObject clone() throws CloneNotSupportedException {
      return data.clone();
    }
    
    @Override
    public HPEObject getFieldValue(Name v) {
      return data.getFieldValue(v);
    }

    @Override
    public String toString() {
      return data.toString();
    }

  }

  private static final HPEObject BOTTOM_OBJECT = new HPEObject(Tag.BOTTOM) {
    @Override
    public HPEObject clone() throws CloneNotSupportedException {
      return this;
    }

    @Override
    public HPEObject getFieldValue(Name v) {
      return this;
    }

    @Override
    public String toString() {
      return "𝈜";
    }

  };
}
