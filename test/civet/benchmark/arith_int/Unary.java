package benchmark.arith_int;

public class Unary extends Exp {
  Exp e;

  Unary(Exp x) {
    e = x;
  }

  Exp getExp() {
    return e;
  }
}
