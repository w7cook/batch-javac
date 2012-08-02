package benchmark.arith_int;

public class Var extends Exp {
  int v;

  Var(int i) {
    v = i;
  }

  int eval(int[] env) {
    return env[v];
  }
}
