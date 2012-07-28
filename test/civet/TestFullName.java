import org.civet.Civet.Compile;


public class TestFullName {

  @Compile
  public static void main(String[] args) {
    test(org.civet.Civet.CT(10), new java.io.BufferedReader(
        new java.io.InputStreamReader(System.in)));
  }

  static void test(int ct, java.io.BufferedReader bufferedReader) {
    System.out.println(org.civet.Civet.RT(ct));
  }

}
