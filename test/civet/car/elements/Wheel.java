package car.elements;

import car.elements.CarElement;
import car.CarElementVisitor;

public class Wheel implements CarElement {
  private String name;

  public Wheel(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void accept(CarElementVisitor visitor) {
    visitor.visit(this);
  }
}