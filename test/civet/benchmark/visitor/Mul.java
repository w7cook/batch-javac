package benchmark.visitor;

public class Mul extends BinOp {
	int apply(int x, int y) {
		return x * y;
	}
}
