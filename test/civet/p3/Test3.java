package p3;

import java.lang.reflect.InvocationTargetException;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Test3 {

  String prop;

  public Test3(String prop) {
    super();
    this.prop = prop;
  }
  
  
  void aMethod(Object o) {
    String propertyValue;
    try {
      propertyValue = (String)o.getClass().getMethod(prop).invoke(o);
      System.out.println(propertyValue);
    } catch (IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Compile
  public static void main(String[] args) {
    Test3 t = Civet.CT(new Test3("toString"));
    t.aMethod(new String("Should be printed!"));
  }
  
}
