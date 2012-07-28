package benchmark.visitor;

public class Aux {
	static int ctr = 0;

	static Tree buildTree(int i) {
		Tree t;
		if (i == 0)
			t = new Leaf(ctr++);
		else
			t = new Node(buildTree(i - 1), buildTree(i - 1));
		return t;
	}
}
