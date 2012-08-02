import org.civet.Civet;
import org.civet.Civet.Compile;


public class SystemOut {

  @Compile
  public static void main(String[] args) {
    int x = Civet.CT(1000);
    System.out.println(x);
  }

}
