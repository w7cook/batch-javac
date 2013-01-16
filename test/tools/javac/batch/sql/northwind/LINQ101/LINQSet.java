package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import batch.sql.Many;
import batch.util.MemSet;
import batch.IncludeInBatch;
import sql.northwind.schema.*;
import batch.Fun;

public class LINQSet extends BaseTest {
  public static void main(String[] args) throws SQLException {
    new LINQSet().run();
  }

  /*Distinct - 1
  
  public void Linq46()
  {
      int[] factorsOf300 = { 2, 2, 3, 5, 5 };
  
      var uniqueFactors = factorsOf300.Distinct();
  
      Console.WriteLine("Prime factors of 300:");
      foreach (var f in uniqueFactors)
      {
          Console.WriteLine(f);
      }
  }*/

  public void Batch46() {
    print("***** Batch46");

    Many<Integer> factorsOf300 = MemSet.make(2, 2, 3, 5, 5);
    print("Prime factors of 300:");
    for (Integer i : factorsOf300.distinct())
      print("{0}", i);
  }

  /*Distinct - 2

  public void Linq47()
  {
      List<Product> products = GetProductList();
  
      var categoryNames = (
          from p in products
          select p.Category)
          .Distinct();
  
      Console.WriteLine("Category names:");
      foreach (var n in categoryNames)
      {
          Console.WriteLine(n);
      }
  }*/

  public void Batch47() {
    print("***** Batch47");

    for (Northwind db : connection) {
      print("Category names:");
      for (Product p : db.Products.distinct())
        print("{0}", p.Category.CategoryName);
    }
  }

  /*Union - 1
  
  public void Linq48()
  {
      int[] numbersA = { 0, 2, 4, 5, 6, 8, 9 };
      int[] numbersB = { 1, 3, 5, 7, 8 };
  
      var uniqueNumbers = numbersA.Union(numbersB);
  
      Console.WriteLine("Unique numbers from both arrays:");
      foreach (var n in uniqueNumbers)
      {
          Console.WriteLine(n);
      }
  }*/

  public void Batch48() {
    /*TODO: union not implemented
    print("***** Batch48");

    Many<Integer> numbersA = MemSet.make(0, 2, 4, 5, 6, 8, 9);
    Many<Integer> numbersB = MemSet.make(1, 3, 5, 7, 8);

    print("Unique numbers from both arrays:");
    	  for (Integer i : numbersA.union(numbersB))
    	    print("{0}", i);
    */
  }

  /*Union - 2
  
  public void Linq49()
  {
      List<Product> products = GetProductList();
      List<Customer> customers = GetCustomerList();
  
      var productFirstChars =
          from p in products
          select p.ProductName[0];
      var customerFirstChars =
          from c in customers
          select c.CompanyName[0];
  
      var uniqueFirstChars = productFirstChars.Union(customerFirstChars);
  
      Console.WriteLine("Unique first letters from Product names and Customer names:");
      foreach (var ch in uniqueFirstChars)
      {
          Console.WriteLine(ch);
      }
  }*/
  public void Batch49() {
    /*TODO: Union
      print("***** Batch48");
    
    for (Northwind db : connection) {
      // TODO: can't get first character
      // TODO: add support for SQL union
      Fun<Product, String> firstCharProductF = new Fun<Product, String>() {@IncludeInBatch public String apply(Product p) {return p.ProductName;}};
      Fun<Category, String> firstCharCategoryF = new Fun<Category, String>() {@IncludeInBatch public String apply(Category c) {return c.CategoryName;}};      
    }
    */
  }

  /*Intersect - 1
  
  public void Linq50()
  {
      int[] numbersA = { 0, 2, 4, 5, 6, 8, 9 };
      int[] numbersB = { 1, 3, 5, 7, 8 };
  
      var commonNumbers = numbersA.Intersect(numbersB);
  
      Console.WriteLine("Common numbers shared by both arrays:");
      foreach (var n in commonNumbers)
      {
          Console.WriteLine(n);
      }
  }*/

  public void Batch50() {
    print("***** Batch50");

    Many<Integer> numbersA = MemSet.make(0, 2, 4, 5, 6, 8, 9);
    Many<Integer> numbersB = MemSet.make(1, 3, 5, 7, 8);

    // TODO: intersect
    print("Common numbers shared by both arrays:");
    //    for (Integer i : numbersA.intersect(numbersB))
    //      print("{0}", i);
  }

  /*Intersect - 2
  
  public void Linq51()
  {
      List<Product> products = GetProductList();
      List<Customer> customers = GetCustomerList();
  
      var productFirstChars =
          from p in products
          select p.ProductName[0];
      var customerFirstChars =
          from c in customers
          select c.CompanyName[0];
  
      var commonFirstChars = productFirstChars.Intersect(customerFirstChars);
  
      Console.WriteLine("Common first letters from Product names and Customer names:");
      foreach (var ch in commonFirstChars)
      {
          Console.WriteLine(ch);
      }
  }*/

  /*public void Batch51() {
    print("***** Batch51");
    
    for (Northwind db : connection) {
      // TODO: can't get first character
      // TODO: add support for SQL intersect
      Fun<Product, String> firstCharProductF = new Fun<Product, String>() {@IncludeInBatch public String apply(Product p) {return p.ProductName;}};
      Fun<Category, String> firstCharCategoryF = new Fun<Category, String>() {@IncludeInBatch public String apply(Category c) {return c.CategoryName;}};      
    }
  }*/

  /*Except - 1

  This sample uses Except to create a sequence that contains the values from numbersAthat are not also in numbersB.

  public void Linq52()
  {
      int[] numbersA = { 0, 2, 4, 5, 6, 8, 9 };
      int[] numbersB = { 1, 3, 5, 7, 8 };

      IEnumerable<int> aOnlyNumbers = numbersA.Except(numbersB);

      Console.WriteLine("Numbers in first array but not second array:");
      foreach (var n in aOnlyNumbers)
      {
          Console.WriteLine(n);
      }
  }

  Result

  Numbers in first array but not second array:
  0
  2
  4
  6
  9
  Except - 2

  This sample uses Except to create one sequence that contains the first letters of product names that are not also first letters of customer names.

  public void Linq53()
  {
      List<Product> products = GetProductList();
      List<Customer> customers = GetCustomerList();

      var productFirstChars =
          from p in products
          select p.ProductName[0];
      var customerFirstChars =
          from c in customers
          select c.CompanyName[0];

      var productOnlyFirstChars = productFirstChars.Except(customerFirstChars);

      Console.WriteLine("First letters from Product names, but not from Customer names:");
      foreach (var ch in productOnlyFirstChars)
      {
          Console.WriteLine(ch);
      }
  }


   */

  public void test() throws SQLException {
    Batch46();
    Batch47();
    /*Batch48();
    Batch49();*/
    Batch50();
  }
}
