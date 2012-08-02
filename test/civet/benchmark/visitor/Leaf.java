package benchmark.visitor;

public class Leaf extends Tree {
	int val;

	Leaf(int x) {
		this.val = x;
	}

	int accept(TreeVisitor t) {
		return t.visitLeaf(this);
	}

	int getVal() {
		return val;
	}
}
