package benchmark.power;

public class Client {
	public static int x(Power pm, Power pa, int base) {
		return pa.raise(pm.raise(base));
	}
}
