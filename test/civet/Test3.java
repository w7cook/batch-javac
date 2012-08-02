import java.util.ArrayList;
import java.util.List;

import org.civet.Civet;
import org.civet.Civet.Compile;


public class Test3 {

  @Compile
  public static void main(String[] args) {
    List<Integer> ints = Civet.CT(new ArrayList<Integer>());
    ints.add(Civet.CT(100));
    int x = Civet.RT(Civet.CT(ints.get(0)));
    System.out.println(x);
  }

}
