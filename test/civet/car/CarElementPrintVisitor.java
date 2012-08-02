package car;

import car.elements.Body;
import car.elements.Engine;
import car.elements.Wheel;

public class CarElementPrintVisitor implements CarElementVisitor {
  int i = 0;

  public void visit(Wheel wheel) {
    System.out.println(++i + ": Printing " + wheel.getName() + " wheel");
  }

  public void visit(Engine engine) {
    System.out.println(++i + ": Printing engine");
  }

  public void visit(Body body) {
    System.out.println(++i + ": Printing body");
  }

  public void visit(Car car) {
    System.out.println(++i + ": Printing car");
  }
}