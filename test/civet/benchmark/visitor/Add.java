package benchmark.visitor;

public class Add extends BinOp {
	int apply(int x, int y) {
		return x + y;
	}
}
