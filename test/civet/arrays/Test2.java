package arrays;

import org.civet.Civet.Compile;

public class Test2 {

  void test1(String p, String... params) {
    System.out.print(p+"(");
    for (String s : params) {
      System.out.print(s+",");
    }
    System.out.println(")");
  }
  
  
  @Compile
  public static void main(String[] args) {
    
    Test2 t = new Test2();
    t.test1("amethod", new String[0]);
    t.test1("amethod", new String[] {});
    t.test1("amethod", new String[] {"a1", "a2"});
  }

}
