package benchmark.fft;

/**************************************************************************
 *                                                                         *
 *             Java Grande Forum Benchmark Suite - Version 2.0             *
 *                                                                         *
 *                            produced by                                  *
 *                                                                         *
 *                  Java Grande Benchmarking Project                       *
 *                                                                         *
 *                                at                                       *
 *                                                                         *
 *                Edinburgh Parallel Computing Centre                      *
 *                                                                         * 
 *                email: epcc-javagrande@epcc.ed.ac.uk                     *
 *                                                                         *
 *                                                                         *
 *      This version copyright (c) The University of Edinburgh, 1999.      *
 *                         All rights reserved.                            *
 *                                                                         *
 **************************************************************************/

import java.util.*;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Main {

  @Compile
  public static void main(String argv[]) {
    Integer in = Civet.CT(16);
    int iters;
    if (argv.length != 2)
      throw new Error("Two integer arguments are needed!");
    try {
      // in = Integer.parseInt(argv[0]);
      iters = Integer.parseInt(argv[1]);
    } catch (NumberFormatException e) {
      throw new Error("Argument must be an integer!");
    }
    JRun(in, iters);
  }

  public static void JKernel(int size, int iters) {
    long RANDOM_SEED = 10101010;
    Random R = new Random(RANDOM_SEED);
    double[] _data = RandomVector(size, R);

    int result = 0;
    Timer tim = new Timer();
    long total_time = 0;
    for (int i = 0; i < 10; i++) {
      System.out.print("[Iteration #" + i + "] ");
      System.out.flush();
      tim.start();
      result = result + performTest(_data, iters, size);
      tim.stop();
      if (i > 4)
        total_time = total_time + tim.diff();
      tim.report();
    }
    System.out.println("Result: " + result);
    System.out.println("Average time 5 last iterations: " + (total_time / 5));
  }

  public static int performTest(double[] _data, int iters, int size) {
    int result = 0;
    for (int i = 0; i < iters; i++) {
      FFT.transform(_data, size);
      for (int j = Civet.CT(0, Civet.IsCT(size)); j < size; j = j + 4)
        result = result + (int) (_data[j] * 100);
    }
    return result;
  }

  public static void TidyUp() {
    System.gc();
  }

  public static void JRun(int size, int inters) {
    JKernel(size, inters);
    TidyUp();
  }

  public static double[] RandomVector(int N, java.util.Random R) {
    double A[] = new double[N];

    for (int i = 0; i < N; i++)
      A[i] = R.nextDouble() * 1e-6;
    return A;
  }
}
