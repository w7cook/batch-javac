package car.elements;

import car.CarElementVisitor;

public class Body implements CarElement {
  public void accept(CarElementVisitor visitor) {
    visitor.visit(this);
  }
}