package benchmark.visitor;

public class FoldBinOp extends TreeVisitor {
	BinOp op;

	FoldBinOp(BinOp x) {
		this.op = x;
	}

	int visitLeaf(Leaf l) {
		return l.val;
	}

	int visitNode(Node n) {
		return op.apply(n.getLeft().accept(this), n.getRight().accept(this));
	}
}
