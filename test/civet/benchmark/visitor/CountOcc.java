package benchmark.visitor;

public class CountOcc extends TreeVisitor {
	int elm;

	CountOcc(int x) {
		this.elm = x;
	}

	int visitLeaf(Leaf l) {
		return this.elm == l.val ? 1 : 0;
	}

	int visitNode(Node n) {
		return n.getLeft().accept(this) + n.getRight().accept(this);
	}
}
