package benchmark.visitor;

import org.civet.Civet;

public class Main {
	public static void main(String argv[]) {
		int size, nIterations;
		if (argv.length != 2)
			throw new Error(
					"Two integer arguments (size and nIterations) is needed!");
		try {
			size = Integer.parseInt(argv[0]);
			nIterations = Integer.parseInt(argv[1]);
		} catch (NumberFormatException e) {
			throw new Error("Arguments must be integers!");
		}
		if (size < 1)
			throw new Error("A positive integer argument is needed!");
		if (nIterations < 1)
			throw new Error("A positive integer argument is needed!");
		int result = 1;
		Timer tim = new Timer();
		Tree t = Aux.buildTree(size);
		Leaf l = findaleaf(t);
		long total_time = 0;
		for (int i = 0; i < 10; i++) {
			System.out.print("[Iteration #" + i + "] ");
			System.out.flush();
			tim.start();
			result = performTest(t, l, size, nIterations, result);
			tim.stop();
			if (i > 4)
				total_time = total_time + tim.diff();
			tim.report();
		}
		System.out.println("Result: " + result);
		System.out.println("Average time 5 last iterations: "
				+ (total_time / 5));
	}

	public static int performTest(Tree t, Leaf l, int size, int nIterations,
			int res) {
		for (int i = 0; i < nIterations; i++) {
			l.val = res;
			res = Main.entrypoint(t);
		}
		return res;
	}

	@Civet.Compile
	public static int entrypoint(Tree t) {
		TreeVisitor v1 = Civet.CT(new FoldBinOp(new Add()));
		TreeVisitor v2 = Civet.CT(new FoldBinOp(new Mul()));
		TreeVisitor v3 = Civet.CT(new CountOcc(0));
		int i1 = test(t, v1);
		int i2 = test(t, v2);
		int i3 = test(t, v3);
		return i1 + i2 + i3;
	}

	public static int test(Tree t, TreeVisitor v) {
		return t.accept(v);
	}

	public static Leaf findaleaf(Tree t) {
		while (!(t instanceof Leaf))
			t = ((Node) t).left;
		return (Leaf) t;
	}
}
