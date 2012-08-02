import org.civet.Civet;
import org.civet.Civet.Compile;

public class TestLocal {

  @Compile
  public static void main(String[] args) {
    
    float s;

    s = Civet.RT(0.0F);
    s = 0.0F;

    System.out.println(s);
    
    int x = Civet.CT(0);
    int y = x;
    System.out.println(y+">>>>");
  }

}
