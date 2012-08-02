package car.elements;

import car.CarElementVisitor;

public class Engine implements CarElement {
  public void accept(CarElementVisitor visitor) {
    visitor.visit(this);
  }
}