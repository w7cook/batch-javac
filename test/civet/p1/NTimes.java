
package p1;

public class NTimes {
  private int n;

  public NTimes(int n) {
    this.n = n;
  }

  public double apply(double x) {
    return Mult.mult(x, n);
  }
}

