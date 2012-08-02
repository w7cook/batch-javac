import org.civet.Civet;
import org.civet.Civet.Compile;

public class Test1 {
  public static double power_rec(double x, int n) {
    if (n == 0)
      return 1;
    else
      return x * power_rec(x, n - 1);
  }

  @Compile
  public static void main(String[] args) {
    double x = Civet.RT(10);
    power_rec(x, Civet.CT(4));
  }
}
