package p2;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class InstanceOf {

  @Compile
  public static void main(String[] args) {
    Object b = Civet.CT(new B());
    if (b instanceof A) {
      System.out.println("b's an A!");
    }
    Object c = Civet.CT(new C());
    if (c instanceof A) {
      System.out.println("c's an A!");
    }
    Object c2 = new C();
    if (c2 instanceof A) {
      System.out.println("c2's an A!");
    }
  }

}

class A {
  
}

class B extends A {
  
}

class C {
  
}