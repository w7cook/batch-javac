package p2;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class InstanceOf2 {
  @Compile
  public static void main(String[] args) {

    if (args instanceof String[]) {
      System.out.println("Yes");
    }
    Object ints = Civet.CT(new int[] { 1, 2, 3 });
    if (ints instanceof int[]) {
      System.out.println("Yes int[]");
    }
    if (ints instanceof byte[]) {
      System.out.println("Yes byte[]");
    }
  }

}
