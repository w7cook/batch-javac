import org.civet.Civet;
import org.civet.Civet.Compile;

public class MultiClass {

  @Compile
  public static void main(String[] args) {
    A a = Civet.CT(new A());
    double y = Civet.RT(9);
    a.mult(Civet.CT(10), y);
  }

}

class A {

  double mult(double x, double y) {
    return x * y;
  }

}
