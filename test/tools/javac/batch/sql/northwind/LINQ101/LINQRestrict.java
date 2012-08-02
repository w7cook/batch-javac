/*******************************************************************************
 * The accompanying source code is made available to you under the terms of 
 * the UT Research License (this "UTRL"). By installing or using the code, 
 * you are consenting to be bound by the UTRL. See LICENSE.html for a 
 * full copy of the license.
 * 
 * Copyright © 2009, The University of Texas at Austin. All rights reserved.
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
package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.schema.Customer;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Order;
import sql.northwind.schema.Product;

public class LINQRestrict extends BaseTest {
public static void main(String[] args) throws SQLException {
  new LINQRestrict().run();
}

 /* Where - Simple 1
 public void Linq1()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      var lowNums =
          from n in numbers
          where n < 5
          select n;
  
      Console.WriteLine("Numbers < 5:");
      foreach (var x in lowNums)
      {
          Console.WriteLine(x);
      }
  }
 */

  public void Batch1() {
    print("***** Batch1");
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    print ("Numbers < 5:");
    for (int n : numbers)
      if (n < 5)
        print("{0}", n);
  }

  /* Where - Simple 2 
   
  public void Linq2()
  {
      List<Product> products = GetProductList();
  
      var soldOutProducts =
          from p in products
          where p.UnitsInStock == 0
          select p;
  
      Console.WriteLine("Sold out products:");
      foreach (var product in soldOutProducts)
      {
          Console.WriteLine("{0} is sold out!", product.ProductName);
      }
  }
  */
  
  public void Batch2() {
    print("***** Batch2");
    for (Northwind db : connection) {
      print("Sold out products:");
      for (Product product : db.Products)
        if (product.UnitsInStock == 0)
          print("{0} is sold out!", product.ProductName);
    }
  }

  /* Where - Simple 3
  public void Linq3()
  {
      List<Product> products = GetProductList();
  
      var expensiveInStockProducts =
          from p in products
          where p.UnitsInStock > 0 && p.UnitPrice > 3.00M
          select p;
  
      Console.WriteLine("In-stock products that cost more than 3.00:");
      foreach (var product in expensiveInStockProducts)
      {
          Console.WriteLine("{0} is in stock and costs more than 3.00.", product.ProductName);
      }
  }
  */
  
  public void Batch3() {
    print("***** Batch3");
    print("In-stock products that cost more than 3.00:");
    for (Northwind db : connection)
      for (Product p : db.Products)
        if (p.UnitsInStock > 0 && p.UnitPrice > 3.00)
          print("{0} is in stock and costs more than 3.00.", p.ProductName);
  }
  
  /* Where - Drilldown
    
  public void Linq4()
  {
      List<Customer> customers = GetCustomerList();
  
      var waCustomers =
          from c in customers
          where c.Region == "WA"
          select c;
  
      Console.WriteLine("Customers from Washington and their orders:");
      foreach (var customer in waCustomers)
      {
          Console.WriteLine("Customer {0}: {1}", customer.CustomerID, customer.CompanyName);
          foreach (var order in customer.Orders)
          {
              Console.WriteLine("  Order {0}: {1}", order.OrderID, order.OrderDate);
          }
      }
  } 
  */

  public void Batch4() {
    print("***** Batch4");
    print("Customers from Washington and their orders:");
    for (Northwind db : connection)
      for (Customer c : db.Customers)
        if (c.Region == "WA") {
          print("Customer {0}: {1}", c.CustomerID, c.CompanyName);
          for (Order order : c.Orders)
            print(" Order {0}: {1}", order.OrderID, order.OrderDate);
        }
  }
  
  /* Where - Indexed
  public void Linq5()
  {
      string[] digits = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
  
      var shortDigits = digits.Where((digit, index) => digit.Length < index);
  
      Console.WriteLine("Short digits:");
      foreach (var d in shortDigits)
      {
          Console.WriteLine("The word {0} is shorter than its value.", d);
      }
  }
  */
  
  public void Batch5() {
    print("***** Batch5");
    String[] digits = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

    print("Short digits:");
    int index = 0;
    for (String d : digits) {
      if (d.length() < index)
        print("The word {0} is shorter than its value.", d);
      index = index + 1;
    }
  }

  public void test() throws SQLException {
    Batch1();
    Batch2();
    Batch3();
    Batch4();
    Batch5();
  }

}
