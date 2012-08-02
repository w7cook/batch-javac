package benchmark.arith_int;

public class Add extends Binary {
  Add(Exp l, Exp r) {
    super(l, r);
  }

  int eval(int[] env) {
    // return getLeft().eval(env)
    // + getRight().eval(env);
    return left.eval(env) + right.eval(env);
  }
}
