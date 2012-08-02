import org.civet.Civet;
import org.civet.Civet.Compile;


public class InstanceMethod {

  @Compile
  public static void main(String[] args) {
    InstanceMethod instanceMethod = new InstanceMethod();
    instanceMethod.mult(Civet.CT(19));    
  }

  int myval;
  
  int mult(int n) {
    myval = myval * n;
    return myval;
  }
  
  
}
