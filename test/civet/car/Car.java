package car;

import java.util.ArrayList;
import java.util.List;

import org.civet.Civet;
import org.civet.Civet.Compile;

import car.elements.*;

/**
 * A slightly modified version of a visitor pattern from
 * 
 * http://en.wikipedia.org/wiki/Visitor_pattern
 */
public class Car implements CarElement {
  int regNo;
  private List<CarElement> elements;

  public Car(int regNo) {
    this.regNo = regNo;
    this.elements = new ArrayList<>();
    this.elements.add(new Engine());
    this.elements.add(new Wheel("front left"));
    this.elements.add(new Wheel("front right"));
    this.elements.add(new Body());
  }

  public void accept(CarElementVisitor visitor) {
    for (CarElement ce : elements) {
      ce.accept(visitor);
    }
    visitor.visit(this);
  }

  @Compile
  public static void main(String[] args) {
    Car car = Civet.CT(new Car(88));

    CarElementVisitor v1 = new CarElementPrintVisitor();
    car.accept(v1);

    CarElementVisitor v2 = new CarElementDoVisitor();
    car.accept(v2);

    Car car2 = new Car(99);
    CarElementVisitor v3 = Civet.CT(new CarElementDoVisitor());
    car2.accept(v3);
  }
}
