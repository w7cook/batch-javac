package benchmark.arith_int;

class Neg extends Unary {
  Neg(Exp x) {
    super(x);
  }

  int eval(int[] env) {
    return -getExp().eval(env);
  }
}
