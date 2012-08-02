package car;

import car.elements.Body;
import car.elements.Engine;
import car.elements.Wheel;

public interface CarElementVisitor {
  void visit(Wheel wheel);

  void visit(Engine engine);

  void visit(Body body);

  void visit(Car car);
}