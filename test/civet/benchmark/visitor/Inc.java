package benchmark.visitor;

public class Inc extends Fun {
	int c;

	Inc(int x) {
		this.c = x;
	}

	int apply(int i) {
		return i + c;
	}
}
