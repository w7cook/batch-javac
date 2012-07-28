package p1;

import org.civet.Civet.Compile;

public class Binary {

  @Compile
  public static void main(String[] args) {

    int x = 10;
    int y = 11;
    int z = 5;
    x *= (y - z);
    System.out.println(x);
  }

}
