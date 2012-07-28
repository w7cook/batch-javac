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
package secureeval;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import batch.IncludeInBatch;
import batch.security.AuthToken;
import batch.security.Group;
import batch.security.Policy;
import batch.security.SecureEval;
import batch.security.User;

public class BasicSecureEval {

	static PrintStream out = System.out;
	/*
	 * 
	 * <E extends java.lang.Object> E twoBar$getRemote(batch.util.Forest s$,
	 * batch.BatchFactory<E> f$, E root, E x) { s$.put("g0", num); E e$ =
	 * f$.Call(f$.Call(f$.Var("root"), "bar", f$.Input("g0")), "bar",
	 * f$.Var("x")); e$ = f$.Let("root", root, e$); e$ = f$.Let("x", x, e$);
	 * return e$; }
	 */
	static int num;

	@IncludeInBatch
	static BasicSecureInterface twoBar(BasicSecureInterface root, int x) {
		out.println("In procedure");
		return root.bar(num).bar(x);
	}

	/*
	 * @Batch(Mode.AfterEffect) void fooBar(TestObj root, int x) {
	 * out.println(root.bar(x).foo(x)); }
	 */
	static int local() {
		return 4;
	}

	static void testBatch1(SecureEval<BasicSecureInterface> server) throws IOException {

		Policy policy = new Policy();
		Group users = new Group("users");
		User bob = new User("bob", Policy.hash("pass"), users);
		User joe = new User("joe", Policy.hash("wontpass"), users);
		policy.addGroup(users);
		policy.addUser(bob);
		policy.addUser(joe);
		server.setPolicy(policy);
		
		AuthToken authToken = new AuthToken("bob", Policy.hash("pass"));
		server.login(authToken);
		
		for (BasicSecureInterface s : server) {
			out.println(s.foo(3));
		}
		for (BasicSecureInterface x : server) {
			System.out.println(x.foo(x.foo(3)));
		}
		for (BasicSecureInterface t : server) {
			System.out.println(t.bar(5).foo(3));
		}
		out.println("ONE REMOTE");
		for (BasicSecureInterface remote : server) {
			out.println(remote.foo(local()));
		}
		out.println("TWO REMOTES");
		for (BasicSecureInterface remote : server) {
			out.println(remote.foo(local()));
			out.println(remote.foo(local()));
		}
		out.println("PROCEDURE");
		for (BasicSecureInterface remote : server) {
			out.println(twoBar(remote, 99).foo(1));
		}
		for (BasicSecureInterface a : server) {
			int x = 5;
			if (x > 2)
				System.out.println(99 + a.foo(5));
			else
				System.out.println("no");
		}
		for (BasicSecureInterface a : server) {
			byte[] buffer = a.getImage("horse");
			FileOutputStream fos = new FileOutputStream("images/outputTest.jpg");
			fos.write(buffer);
			fos.close();
		}
	}

	public static void main(String[] args) throws IOException {
		testBatch1(new SecureEval<BasicSecureInterface>(new BasicSecureObj(1)));
	}
}
