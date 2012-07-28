import org.civet.Civet;
import org.civet.Civet.Compile;

public class TestStaticCall {

  @Compile
  public static void main(String[] args) {

    System.out.println(log2(Civet.CT(1024)));

    System.out.println(test2(2, 3));
    System.out.println(test2(Civet.CT(4), 3));
  }

  static int test2(int x, int y) {
    return x * y;
  }

  static int log2(int n) {
    int log = 0;
    for (int k = 1; k < n; k = k * 2, log++)
      ;
    if (n != (1 << log))
      throw new Error("Is not a power of 2!: " + n);
    return log;
  }

}
