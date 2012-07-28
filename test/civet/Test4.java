import org.civet.Civet;
import org.civet.Civet.Compile;


public class Test4 {

  @Compile
  public static void main(String[] args) {
    
    int x = Civet.CT(10);
    
    int i = Civet.CT(4, Civet.IsCT(x));
    while (i > 0) {
      System.out.println(x);
      i = i - 1;
    }
    
    int y = 10;
    int j = Civet.CT(4, Civet.IsCT(y));
    while (j > 0) {
      System.out.println(y);
      j = j - 1;
    }
    
  }

}
