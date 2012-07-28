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
package tcp;
/*
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import batch.eval.BasicInterface;
import batch.secureeval.BasicSecureInterface;
import batch.secureeval.BasicSecureObj;
import batch.json.JSONTransport;
import batch.security.AuthToken;
import batch.security.Group;
import batch.security.Policy;
import batch.security.SecureEval;
import batch.security.User;

import batch.tcp.SecureTCPClient;
import batch.tcp.SecureTCPServer;
import batch.util.BatchTransport;
import batch.xml.XMLTransport;
import batch.syntax.build.DynamicFactory;

public class SecureTCP {

	public static void main(String argv[]) throws Exception {
		testTCP(9827, new JSONTransport<Expression>());
		testTCP(9829, new XMLTransport<Expression>());
		System.exit(0);
	}

	private static void testTCP(int port, BatchTransport<Expression> transport)
			throws IOException, UnknownHostException {
		
		// setup batch server 
		// start with creating the handler, a secure eval
		transport.setFactory(new DynamicFactory<Expression>(batch.syntax.Factory.factory));
		BasicSecureObj root = new BasicSecureObj(1000);
		SecureEval<BasicSecureInterface> handler = new SecureEval<BasicSecureInterface>(root);
		// create the policy for secure eval
		Policy policy = new Policy();
		Group users = new Group("users");
		User bob = new User("bob", Policy.hash("pass"), users);
		User joe = new User("joe", Policy.hash("wontpass"), users);
		policy.addGroup(users);
		policy.addUser(bob);
		policy.addUser(joe);
		handler.setPolicy(policy);
		// create the tcp server
		SecureTCPServer<Expression, BasicSecureInterface> server = 
				new SecureTCPServer<Expression,	BasicSecureInterface>(
						handler, new ServerSocket(port), transport);
		server.start();

		// setup batch client
		SecureTCPClient<BasicInterface> service = 
				new SecureTCPClient<BasicInterface>(InetAddress.getLocalHost(),
						port, transport);
		service.login(new AuthToken("bob", Policy.hash("pass")));
//		service.login(new AuthToken("joe", Policy.hash("wontpass")));
		
		// use the service
		for (BasicInterface remote : service) {
			System.out.println("got remote value: " + remote.foo(3));
		}

		for (BasicInterface x : service) {
			System.out.println(x.foo(x.foo(3)));
		}
		
		for (BasicInterface a : service) {
			byte[] buffer = a.getImage("horse");
			FileOutputStream fos = new FileOutputStream("images/outputTest.jpg");
			fos.write(buffer);
			fos.close();
		}

	}

}
*/
