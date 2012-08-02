package benchmark.visitor;

public class Node extends Tree {
	Tree left, right;

	Node(Tree x, Tree y) {
		this.left = x;
		this.right = y;
	}

	int accept(TreeVisitor v) {
		return v.visitNode(this);
	}

	Tree getLeft() {
		return left;
	}

	Tree getRight() {
		return right;
	}
}
