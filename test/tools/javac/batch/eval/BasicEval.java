/*******************************************************************************
 * The accompanying source code is made available to you under the terms of 
 * the UT Research License (this "UTRL"). By installing or using the code, 
 * you are consenting to be bound by the UTRL. See LICENSE.html for a 
 * full copy of the license.
 * 
 * Copyright 2009, The University of Texas at Austin. All rights reserved.
 * 
 * UNIVERSITY EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES CONCERNING THIS 
 * SOFTWARE AND DOCUMENTATION, INCLUDING ANY WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR ANY PARTICULAR PURPOSE, NON-INFRINGEMENT AND WARRANTIES 
 * OF PERFORMANCE, AND ANY WARRANTY THAT MIGHT OTHERWISE ARISE FROM COURSE 
 * OF DEALING OR USAGE OF TRADE. NO WARRANTY IS EITHER EXPRESS OR IMPLIED 
 * WITH RESPECT TO THE USE OF THE SOFTWARE OR DOCUMENTATION. Under no circumstances 
 * shall University be liable for incidental, special, indirect, direct 
 * or consequential damages or loss of profits, interruption of business, 
 * or related expenses which may arise from use of Software or Documentation, 
 * including but not limited to those resulting from defects in Software 
 * and/or Documentation, or loss or inaccuracy of data of any kind.
 * 
 * Created by: William R. Cook and Eli Tilevich
 * with: Jose Falcon, Marc Fisher II, Ali Ibrahim, Yang Jiao, Ben Wiedermann
 * University of Texas at Austin and Virginia Tech
 ******************************************************************************/
package eval;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import batch.EvalService;
import batch.IncludeInBatch;

public class BasicEval {
/*  void main() {
    batch.Service<batch.syntax.Evaluate, eval.BasicInterface> s = null;
    batch.util.BatchFactory<batch.syntax.Evaluate> t = s;
  }
}
*/

	
	static PrintStream out = System.out;
	static int num;

	
	@IncludeInBatch
	static BasicInterface twoBar(BasicInterface root, int x) {
		out.println("In procedure");
		return root.bar(num).bar(x);
	}

	static int local() {
		return 4;
	}
	
	static <E> E test(E a, E b) {
	  List<E> x = new ArrayList<E>();
	  x.add(a);
	  x.add(b);
	  return null;
	}

	static void testBatch1(EvalService<BasicInterface> server) throws IOException {

		for (BasicInterface s : server) {
			out.println("test " + s.foo(3));
		}
		for (BasicInterface x : server) {
			System.out.println(x.foo(x.foo(3)));
		}
		for (BasicInterface t : server) {
			System.out.println(t.bar(5).foo(3));
		}
		out.println("ONE REMOTE");
		for (BasicInterface remote : server) {
			out.println(remote.foo(local()));
	}
    out.println("TWO REMOTES");
    for (BasicInterface remote : server) {
      out.println(remote.foo(local()));
      out.println(remote.foo(local()));
    }

    out.println("PROCEDURE");
		for (BasicInterface remote : server) {
			out.println(twoBar(remote, 99).foo(1));
		}

		for (BasicInterface a : server) {
			int x = 5;
			if (x > 2)
				System.out.println(99 + a.foo(5));
			else
				System.out.println("no");
		}
		for (BasicInterface a : server) {
			byte[] buffer = a.getImage("horse");
			FileOutputStream fos = new FileOutputStream("test/images/outputTest.jpg");
			fos.write(buffer);
			fos.close();
		}
	}

	public static void main(String... args) throws Exception {
		testBatch1(new EvalService<BasicInterface>(new BasicObj(1)));
	}
}
