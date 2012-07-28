package p3;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Test2 {

  @Compile
  public static void main(String[] args) {
    Circle c1 = Civet.CT(new Circle());
    System.out.println("" + c1);
    c1.p = Civet.CT(new Point(100, 200));
    Circle c12 = c1;
    System.out.println(c1.toString());
    c12.r = Civet.CT(12);
    System.out.println(c1.toString());
  }

}
