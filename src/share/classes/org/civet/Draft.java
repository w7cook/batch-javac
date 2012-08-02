package org.civet;

public class Draft {

  
  enum Test { A1, A2, A3 };
  
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    System.out.println(Test.class.isEnum());
    for (Test x : Test.class.getEnumConstants()) {
      System.out.println(x);
    }
  }

}
