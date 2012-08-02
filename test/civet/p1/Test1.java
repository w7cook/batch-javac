
package p1;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Test1 {
  @Compile
  public static void main(String[] args) {
    NTimes fiveTimes = Civet.CT(new NTimes(5));
    double x = Civet.RT(9);
    fiveTimes.apply(x);
  }
}

