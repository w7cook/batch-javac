package benchmark.power;

import org.civet.Civet;

public class Power extends Object {
	int exp;
	Binary op;
	int neutral;

	Power(int exp, Binary op, int neutral) {
		super();
		this.exp = exp;
		this.op = op;
		this.neutral = neutral;
	}

	int raise(int base) {
		// rewritten to use a loop (only sensible thing in Java)
		int res = Civet.RT(neutral);		
    for (int i = Civet.CT(0, Civet.IsCT(this)); i < exp; i++) {
      res = op.eval(base, res);
    }
		return res;
	}
}

class Binary extends Object {
	Binary() {
		super();
	}

	int eval(int x, int y) {
		return this.eval(x, y);
	}
}

class Add extends Binary {
	Add() {
		super();
	}

	int eval(int x, int y) {
		return x + y;
	}
}

class Mult extends Binary {
	Mult() {
		super();
	}

	int eval(int x, int y) {
		return x * y;
	}
}
