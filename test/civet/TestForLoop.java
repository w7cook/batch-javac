import org.civet.Civet;
import org.civet.Civet.Compile;


public class TestForLoop {

  
  @Compile
  void test1() {
    int x = 10;
    for(int i = 0; i < 10; i++) {
      System.out.println(x+i);
    }
  }
  
  @Compile
  void test2() {
    int x = 10;
    for(int i = Civet.CT(0); i < 10; i++) {
      System.out.println(x+i);
    }
  }  
}
