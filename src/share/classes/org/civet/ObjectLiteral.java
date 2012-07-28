package org.civet;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.sun.tools.javac.util.Name;

public class ObjectLiteral extends HPEObject {

  public ObjectLiteral(Object v, Boolean isArray) {
    object = v;
    kind = Kind.OBJECT;
    this.isArray = isArray;
  }

  public HPEObject getArrayItem(int i) {
    assert (object != null);
    try {
      Object val = java.lang.reflect.Array.get(object, i);
      return HPEObject.fromObject(val).setTag(this.tag);
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    throw new HPEException("Could not find array index " + this.toString());
  }
  

  @Override
  public HPEObject getFieldValue(Name fieldName) {
    if (fieldName.toString().equals("this")) {
      return this;
    } else {
      Class<?> clazz = object.getClass();
      if (clazz.isArray() && fieldName.toString().equals("length")) {
        int len = java.lang.reflect.Array.getLength(object);
        return HPEObject.fromObject(len).setTag(this.tag);
      }
      try {
        Field f = hasField(clazz, fieldName.toString());
        if (f == null) {
          return null;
        }
        f.setAccessible(true);

        Object val = f.get(object);
        return HPEObject.fromObject(val).setTag(this.tag)
            .convertToField(fieldName, this);
      } catch (SecurityException | IllegalArgumentException
          | IllegalAccessException e) {
        throw new HPEException(e);
      }
    }
  }

  @Override
  public boolean hasField(Name v) {
    String fieldName = v.toString();
    assert (object != null);
    java.lang.Class<?> c = null;
    c = object.getClass();
    return hasField(c, fieldName) != null;
  }

  @Override
  public void setFieldValue(Name x, HPEObject v) {
    if (!v.isConcrete())
      throw new HPEException("Unable to assign a non-literal to object "
          + object);

    String fieldName = x.toString();

    assert (object != null);
    try {
      java.lang.Class<?> c = this.object.getClass();
      java.lang.reflect.Field f = c.getDeclaredField(fieldName);
      f.setAccessible(true);
      f.set(object, convertNumTypes(v.object, f.getType()));
    } catch (Exception e) {
      e.printStackTrace();
      throw new HPEException("Could not find field " + fieldName
          + " in object " + this.toString());
    }
  }

  
  public void setArrayItem(int i, HPEObject v) {
    assert (object != null);
    try {
      java.lang.reflect.Array.set(object, i, v.object);
    } catch (SecurityException e) {
      e.printStackTrace();
    }
  }
  

  public static Object convertNumTypes(Object v, Class<?> to) {
    if (to == int.class || to == Integer.class || to == long.class
        || to == Long.class || to == short.class || to == Short.class
        || to == byte.class || to == Byte.class || to == double.class
        || to == Double.class || to == float.class || to == Float.class) {
      BigDecimal d = new BigDecimal(v.toString());
      if (to == int.class || to == Integer.class) {
        return new Integer(d.intValue());
      }
      if (to == long.class || to == Long.class) {
        return new Long(d.longValue());
      }
      if (to == short.class || to == Short.class) {
        return new Short(d.shortValue());
      }
      if (to == byte.class || to == Byte.class) {
        return new Byte(d.byteValue());
      }
      if (to == double.class || to == Double.class) {
        return new Double(d.doubleValue());
      }
      if (to == float.class || to == Float.class) {
        return new Float(d.floatValue());
      }
    }
    return v;
  }

  @Override
  public HPEObject clone() throws CloneNotSupportedException {
    throw new NotImplementedException();
  }

  @Override
  public String toString() {
    throw new NotImplementedException();
  }

}