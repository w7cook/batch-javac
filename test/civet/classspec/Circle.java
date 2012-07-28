package classspec;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Circle {

  int radius;
  Point center;

  public Circle() {
  }

  public Circle(int radius, Point center) {
    this.radius = radius;
    this.center = center;
  }

  @Compile
  public static void main(String[] args) {
    Circle c1 = new Circle(Civet.CT(10), new Point(1, 1));
    System.out.println(c1.getArea());
  }

  double getArea() {
    return Math.PI * radius * radius;
  }

  double getPerimeter() {
    return 2 * Math.PI * radius;
  }

}

class Point {

  double x, y;

  public Point(double x, double y) {
    super();
    this.x = x;
    this.y = y;
  }

}