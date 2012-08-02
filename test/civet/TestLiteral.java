import org.civet.Civet;
import org.civet.Civet.Compile;


public class TestLiteral {

  @Compile
  void test1() {
    int x = 10;
    int i = 1;
    System.out.println(x + i);
  }
  
  @Compile
  void test2() {
    int x = 10;
    int i = Civet.CT(1);
    System.out.println(x + i);
  }
  
  @Compile
  void test3() {
    int x = 10;
    double i = Civet.CT(1.0);
    System.out.println(x + i);
  }
  
  
}
