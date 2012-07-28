package arrays;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Vec {
	double[] v;

	public Vec(double[] v) {
		this.v = v;
	}

	double get(int i) {
		return v[i];
	}

	void set(int i, double d) {
		v[i] = d;
	}

	int size() {
		return v.length;
	}

	public double dotP(Vec v2) {
		double res = 0.0;
		for (int i = Civet.CT(0, Civet.IsCT(this)); i < size(); i++) {
			res = res + get(i) * v2.get(i);
		}
		return res;
	}
	
	@Compile
	public static void main(String[] args) {
		double[] fs = Civet.CT(new double[]{ 1.0, 0.0, 3.0 });
		Vec v1 = Civet.CT(new Vec(fs));
		Vec v2 = new Vec(new double[] {0.6, 9.2, 4.0});
		double r = v1.dotP(v2);
		System.out.println(r);
	}
}
