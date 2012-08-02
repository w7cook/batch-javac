package benchmark.arith_int;

public class Binary extends Exp {

  Exp left, right;

  Binary(Exp l, Exp r) {
    left = l;
    right = r;
  }

  Exp getLeft() {
    return left;
  }

  Exp getRight() {
    return right;
  }
}
