package benchmark.romberg;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Main {
  @Compile
  public static void main(String argv[]) {
    Main m = new Main();
    Integer size = Civet.CT(2);
    int nIterations;
    if (argv.length != 2)
      throw new Error("Two integer arguments (size and nIterations) is needed!");
    try {
      size = 2;
      nIterations = Integer.parseInt(argv[1]);
    } catch (NumberFormatException e) {
      throw new Error("Arguments must be integers!");
    }
    if (size < 1)
      throw new Error("A positive integer argument is needed!");
    if (nIterations < 1)
      throw new Error("A positive integer argument is needed!");
    m.array = new float[size + 1][size + 1];
    float result = 1.0F;
    Timer tim = new Timer();
    Function f = new Square();
    long total_time = 0;
    for (int i = 0; i < 10; i++) {
      System.out.print("[Iteration #" + i + "] ");
      System.out.flush();
      tim.start();
      result = m.performTest(f, size, nIterations, result);
      tim.stop();
      if (i > 4)
        total_time = total_time + tim.diff();
      tim.report();
    }
    System.out.println("Result: " + result);
    System.out.println("Average time 5 last iterations: " + (total_time / 5));
  }

  float[][] array;

  public float performTest(Function f, int size, int nIterations,
      float res) {
    for (int i = 0; i < nIterations; i++) {
      Romberg.romberg(array, res, res + 1.0F, size, f);
      res = array[0][0];
    }
    return res;
  }
}
