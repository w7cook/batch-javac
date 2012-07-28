package benchmark.arith_int;

public class Apply {
  public static int a(Exp e, int[] env) {
    return e.eval(env);
  }
}
