package org.civet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

public class CLASS extends HPEObject {
  private ClassSymbol classSymbol;
  private Class<?> clazz;

  public Type getType() {
    if (isArray)
      return arrayType;
    else
      return classSymbol.type;
  }

  private ArrayType arrayType;

  public CLASS(ClassSymbol classSymbol) {
    this.kind = Kind.CLASS;
    this.classSymbol = classSymbol;
    clazz = HPEUtil.toClass(classSymbol.type);
    this.object = clazz;
  }

  public CLASS(ArrayType type) {
    this.kind = Kind.CLASS;
    isArray = true;
    arrayType = type;
  }

  @Override
  public HPEObject getFieldValue(Name fieldName) {
    if (fieldName.equals("class")) {
      return this;
    } else {
      try {
        Field f = hasField(clazz, fieldName.toString());
        if (f == null) {
          return null;
        }
        f.setAccessible(true);

        Object val = f.get(null);
        return HPEObject.fromObject(val).setTag(this.tag)
            .convertToField(fieldName, this);
      } catch (SecurityException | IllegalArgumentException
          | IllegalAccessException e) {
        throw new HPEException(e);
      }
    }
  }

  @Override
  public HPEObject clone() throws CloneNotSupportedException {
    throw new NotImplementedException();
  }

  @Override
  public String toString() {
    return classSymbol.toString();
  }

  public HPEObject newInstance(Type constructorType, List<HPEObject> args) {
    Class<?>[] paramClasses = new Class<?>[args.length()];
    try {
      int i = 0;
      Object[] argObjects = new Object[args.length()];
      for (Type t : constructorType.getParameterTypes()) {
        paramClasses[i] = HPEUtil.toClass(t);
        argObjects[i] = args.get(i).object;
        i++;
      }
      Constructor<?> constructor = clazz.getDeclaredConstructor(paramClasses);
      constructor.setAccessible(true);
      Object o = constructor.newInstance(argObjects);
      return HPEObject.fromObject(o);
    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      throw new HPEException(e);
    }
  }

}