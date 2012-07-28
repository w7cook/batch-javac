package benchmark.visitor;

public class Reset extends Fun {
	int fixed;

	Reset(int x) {
		this.fixed = x;
	}

	int apply(int i) {
		return this.fixed;
	}
}
