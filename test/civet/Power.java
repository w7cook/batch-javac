import org.civet.Civet;
import org.civet.Civet.Compile;


public class Power {
  
  private int exponent;
  
  public Power(int exponent) {
    super();
    this.exponent = exponent;
  }

  public double power(double x) {
    double p = 1;
    int n = exponent;
    while (n > 0) {
      p = p * x;
      n = n - 1;
    }
    return p;
  }

  @Compile
  public static void main(String[] args) {
    Power p = Civet.CT(new Power(4));
    int x = Civet.RT(10);
    p.power(x);
  }
  
}
