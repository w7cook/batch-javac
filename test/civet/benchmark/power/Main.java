package benchmark.power;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Main {
  
  @Compile
	public static void main(String argv[]) {
		int nIterations;
		if (argv.length != 1)
			throw new Error(
					"A single integer argument (nIterations) is needed!");
		try {
			nIterations = Integer.parseInt(argv[0]);
		} catch (NumberFormatException e) {
			throw new Error(
					"Argument must be an integer! (the number of iterations)");
		}
		if (nIterations < 1)
			throw new Error("A positive integer argument is needed!");
		Power pm = Civet.CT(new Power(16, new Mult(), 1));
		Power pa = Civet.CT(new Power(16, new Add(), 0));
		int result = 2;
		Timer tim = new Timer();
		long total_time = 0;

		int i = 0; 
    while (i < 10) {
			System.out.print("[Iteration #" + i + "] ");
			System.out.flush();
			tim.start();
			result = performTest(pm, pa, nIterations, result);
			tim.stop();
			if (i > 4)
				total_time = total_time + tim.diff();
			tim.report();
			i++;
		}
		System.out.println("Result: " + result);
		System.out.println("Average time 5 last iterations: "
				+ (total_time / 5));
	}

	public static int performTest(Power pm, Power pa, int nIterations, int res) {
		for (int i = 0; i < nIterations; i++)
			res = res + Client.x(pm, pa, res % 4);
		return res;
	}
}
