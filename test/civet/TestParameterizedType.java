import java.util.ArrayList;
import java.util.List;

import org.civet.Civet.Compile;

public class TestParameterizedType {

  @Compile
  public static void main(String[] args) {
    List<Integer> l = new ArrayList<Integer>();
    l.add(5);
    l.add(15);
    l.add(25);    
  }

}
