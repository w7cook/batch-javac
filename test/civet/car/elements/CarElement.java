package car.elements;

import car.CarElementVisitor;

public interface CarElement {
  void accept(CarElementVisitor visitor);
}