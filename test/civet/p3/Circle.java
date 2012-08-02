package p3;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Circle {

  int r = 0;
  Point p = null;

  @Compile
  public static void main(String[] args) {
    Circle c1 = Civet.CT(new Circle());
    // c1.r = 10;
    // c1.p = new Point(100, 200);
    System.out.println("" + c1);
    c1.p = Civet.CT(new Point(100, 200));
    System.out.println(c1.toString());
  }

  @Override
  public String toString() {
    String pStr = "";
    if (p != null)
      pStr = p.toString();
    return "Circle(" + r + "; " + pStr + ")";
  }

}

class Point {
  final double x, y;

  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
