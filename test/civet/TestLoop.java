import org.civet.Civet.Compile;


public class TestLoop {

  @Compile
  void test1() {
    int i = 0; 
    while (i < 10) {
      i++;
    }    
  }
  
  @Compile
  void test2() {
    int i = 0; 
    while (i < 10) {
      i--;
    }    
  }
  
  
}
