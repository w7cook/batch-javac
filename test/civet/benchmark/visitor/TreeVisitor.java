package benchmark.visitor;

public abstract class TreeVisitor {
	abstract int visitLeaf(Leaf f);

	abstract int visitNode(Node n);
}
