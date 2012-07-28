import org.civet.Civet;
import org.civet.Civet.Compile;

public class TestForLoop2 {
  
  public static void main(String[] args) {
    TestForLoop2 t = new TestForLoop2();
    t.test2();
  }
  
  @Compile
  void test1() {
    for (int i = Civet.CT(0), j = 99; i < 10; i++, j--) {
      System.out.println(i + j);
    }
  }
  @Compile
  void test2() {
    for (int i = Civet.CT(0), j = 99; i < 10; i++, j++) {
      System.out.println(i + j);
    }
  }
}
