import java.util.ArrayList;
import java.util.List;

import org.civet.Civet;
import org.civet.Civet.Compile;


public class FancyFor {

  @Compile
  public static void main(String[] args) {
    List<Integer> ints = Civet.CT(new ArrayList<Integer>());
    ints.add(Civet.CT(100));
    ints.add(Civet.CT(200));
    ints.add(Civet.CT(300));
    for (int x : ints) {
      System.out.println(Civet.RT(x));
    }
  }

}
