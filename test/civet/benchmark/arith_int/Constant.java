package benchmark.arith_int;

public class Constant extends Exp {
  int c;

  Constant(int i) {
    c = i;
  }

  int eval(int[] env) {
    return c;
  }
}
