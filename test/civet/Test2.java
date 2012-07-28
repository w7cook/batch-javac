import org.civet.*;
import org.civet.Civet.Compile;

public class Test2 {
  public static double power(double x, int n) {
    double p = 1;
    while (n > 0) {
      p = p * x;
      n = n - 1;
    }
    return p;
  }

  @Compile
  public static void main(String[] args) {
    double x = Civet.RT(10);
    power(x, Civet.CT(5));
  }
}
