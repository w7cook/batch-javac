package org.civet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeKind;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.util.List;

public class CLOSURE extends HPEObject {
  HPEObject thiz;
  MethodSymbol methodSymbol;

  public CLOSURE(HPEObject o, MethodSymbol ms) {
    thiz = o;
    methodSymbol = ms;
    this.kind = Kind.CLOSURE;
  }

  public CLOSURE(MethodSymbol ms) {
    this(null, ms);
  }

  public boolean isCTMethod() {
    assert (methodSymbol != null && methodSymbol.owner != null);
    return methodSymbol.name.toString().equals("CT")
        && methodSymbol.owner.getKind() == ElementKind.CLASS
        && ((ClassSymbol) methodSymbol.owner).fullname.toString().equals(
            "org.civet.Civet") && methodSymbol.getParameters().length() == 1;
  }

  public boolean isCTIfMethod() {
    assert (methodSymbol != null && methodSymbol.owner != null);
    return methodSymbol.name.toString().equals("CT")
        && methodSymbol.owner.getKind() == ElementKind.CLASS
        && ((ClassSymbol) methodSymbol.owner).fullname.toString().equals(
            "org.civet.Civet") && methodSymbol.getParameters().length() == 2;
  }

  public boolean isIsCTMethod() {
    assert (methodSymbol != null && methodSymbol.owner != null);
    return methodSymbol.name.toString().equals("IsCT")
        && methodSymbol.owner.getKind() == ElementKind.CLASS
        && ((ClassSymbol) methodSymbol.owner).fullname.toString().equals(
            "org.civet.Civet") && methodSymbol.getParameters().length() == 1;
  }

  public boolean isRTMethod() {
    assert (methodSymbol != null && methodSymbol.owner != null);
    return methodSymbol.name.toString().equals("RT")
        && methodSymbol.owner.getKind() == ElementKind.CLASS
        && ((ClassSymbol) methodSymbol.owner).fullname.toString().equals(
            "org.civet.Civet");
  }

  @Override
  public String toString() {
    return methodSymbol.toString();
  }

  public HPEObject invoke(List<HPEObject> args) {
    Object thisObject = null;
    if (!methodSymbol.isStatic()) {
      thisObject = thiz.object;
    }
    ClassSymbol classSymbol = (ClassSymbol) methodSymbol.owner;
    try {
      Class<?> clazz = Class.forName(classSymbol.getQualifiedName().toString());
      MethodType methodType = (MethodType) methodSymbol.type;
      Class<?>[] paramClasses = new Class<?>[args.length()];
      Object[] argObjects = new Object[args.length()];
      int i = 0;
      for (Type t : methodType.getParameterTypes()) {
        Class<?> c;
        if (t.getKind() == TypeKind.TYPEVAR) {
          c = HPEUtil.toClass(((TypeVar) t).bound);
        } else {
          c = HPEUtil.toClass(t);
        }
        paramClasses[i] = c;
        argObjects[i] = args.get(i).object;
        i++;
      }
      Method m = clazz.getDeclaredMethod(methodSymbol.name.toString(),
          paramClasses);
      m.setAccessible(true);
      Object result = m.invoke(thisObject, argObjects);
      return fromObject(result);
    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
        | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      throw new HPEException(e);
    }
  }
}