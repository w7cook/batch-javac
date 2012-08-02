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

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import batch.Service;
import batch.json.JSONTransport;
import batch.tcp.TCPClient;
import batch.util.BatchTransport;
import batch.xml.XMLTransport;
import eval.BasicInterface;

public class SimpleTCP {

	public static void main(String argv[]) throws Exception {
    System.out.println("Connecting to server");
    String format = argv[0];
    String server = argv[1];
    int port = Integer.parseInt(argv[2]);
    if (format.equals("JSON"))
    	testTCP(server, port, new JSONTransport());
    else if (format.equals("XML"))
    	testTCP(server, port, new XMLTransport());
    else
    	System.out.println("Unknown format: " + format);
    
    System.out.println("Finished");
	}

	private static void testTCP(String server, int port, BatchTransport transport)
			throws IOException, UnknownHostException {

		TCPClient<BasicInterface> service = new TCPClient<BasicInterface>(
				InetAddress.getByName(server), port, transport);
		for (BasicInterface remote : service) {
			System.out.println("got remote value: " + remote.foo(3));
		}

		for (BasicInterface x : service) {
			System.out.println(x.foo(x.foo(3)));
		}
		for (BasicInterface a : service) {
			byte[] buffer = a.getImage("horse");
			FileOutputStream fos = new FileOutputStream("test/images/outputTest.jpg");
			fos.write(buffer);
			fos.close();
		}
		
		listFiles(service);
		
	}

	static void listFiles(TCPClient<BasicInterface> dirServer) {
    for (BasicInterface root : dirServer) {
      java.io.File dir = root.getDir();
      System.out.println("Large files in " + dir.getName());
      for (java.io.File file : dir.listFiles())
        if (file.length() > 1000) {
          System.out.println(file.getPath());
        }
    }
  }

}
