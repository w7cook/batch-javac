package arrays;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Test {

	String[] a = new String[3];
	String b = "";
	
	int[] c = new int[] {1, 2, 3, 4};

	@Compile
	public static void main(String[] args) {
		Test t = Civet.CT(new Test());
		t.b = "a" +"b";
		t.a[2] = "c" + "d";
		t.a[1] = "123";
		int[] m = Civet.CT(new int[2]);
		m[0] = 10;
		System.out.println(m[0]);
		
		int[] w = Civet.CT(new int[] {1, 2, 3, 4});
		
		System.out.println(w.length);
		System.out.println(w[3]);
		w[3] = 1000;
		System.out.println("w[3] = "+w[3]);
		System.out.println(m.length);
		t.c[2] = 99;
		if (t.c[2] == 99) {
			System.out.println("hoorayyyyyyyyyyyyy");
		}
		System.out.println(t.c[2]);
		System.out.println(t.b+t.a[2]);

		
		System.out.println(t.c.length);
		
	}

}
