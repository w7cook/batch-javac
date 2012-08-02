package sql.northwind.LINQ101;

import java.sql.Date;
import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.schema.Customer;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Order;
import sql.northwind.schema.Order_Details;
import sql.northwind.schema.Product;

public class LINQProject extends BaseTest {
  public static void main(String[] args) throws SQLException {
    new LINQProject().run();
  }
  
 /* Select - Simple 1
  public void Linq6()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      var numsPlusOne =
          from n in numbers
          select n + 1;
  
      Console.WriteLine("Numbers + 1:");
      foreach (var i in numsPlusOne)
      {
          Console.WriteLine(i);
      }
  }
  */
  
  public void Batch6() {
    print("***** Batch6");
  
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
    print("Numbers + 1:");
    for (int i : numbers)
      print("{0}", i + 1);
  }
  
  /* Select - Simple 2
   public void Linq7()
  {
      List<Product> products = GetProductList();
  
      var productNames =
          from p in products
          select p.ProductName;
  
      Console.WriteLine("Product Names:");
      foreach (var productName in productNames)
      {
          Console.WriteLine(productName);
      }
  }
  */
  
  public void Batch7() {
    print("***** Batch7");
    print("Product Names:");
    for (Northwind db : connection)
      for (Product p : db.Products)
        print("{0}", p.ProductName);
  }
  
  /* Select - Transformation
   public void Linq8()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
      string[] strings = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
  
      var textNums =
          from n in numbers
          select strings[n];
  
      Console.WriteLine("Number strings:");
      foreach (var s in textNums)
      {
          Console.WriteLine(s);
      }
  }
   */
  
  public void Batch8() {
    print("***** Batch8");
  
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    String[] strings = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
  
    print("Number strings:");
    for (int n : numbers)
      print(strings[n]);
  }
 
  /* Select - Anonymous Types 1 
   
  public void Linq9()
  {
      string[] words = { "aPPLE", "BlUeBeRrY", "cHeRry" };
  
      var upperLowerWords =
          from w in words
          select new { Upper = w.ToUpper(), Lower = w.ToLower() };
  
      foreach (var ul in upperLowerWords)
      {
          Console.WriteLine("Uppercase: {0}, Lowercase: {1}", ul.Upper, ul.Lower);
      }
  }
   */
  
  public void Batch9() {
    print("***** Batch9");
  
    String[] words = { "aPPLE", "BlUeBeRrY", "cHeRry" };
    for (String w : words)
      print("Uppercase: {0}, Lowercase: {1}", w.toUpperCase(), w.toLowerCase());
  }
  
  /* Select - Anonymous Types 2
   
  public void Linq10()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
      string[] strings = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
  
      var digitOddEvens =
          from n in numbers
          select new { Digit = strings[n], Even = (n % 2 == 0) };
  
      foreach (var d in digitOddEvens)
      {
          Console.WriteLine("The digit {0} is {1}.", d.Digit, d.Even ? "even" : "odd");
      }
  }
   */
  
  public void Batch10() {
    print("***** Batch10");
  
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    String[] strings = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
  
    for (int n : numbers)
      print("The digit {0} is {1}.", strings[n], (n % 2 == 0) ? "even" : "odd");
  }
  
  /* Select - Anonymous Types 3
  
   public void Linq11()
    {
        List<Product> products = GetProductList();
    
        var productInfos =
            from p in products
            select new { p.ProductName, p.Category, Price = p.UnitPrice };
    
        Console.WriteLine("Product Info:");
        foreach (var productInfo in productInfos)
        {
            Console.WriteLine("{0} is in the category {1} and costs {2} per unit.", productInfo.ProductName, productInfo.Category, productInfo.Price);
        }
    }
   */
  public void Batch11() {
    print("***** Batch11");
  
    print("Product Info:");
    for (Northwind db : connection)
      for (Product p : db.Products)
        print("{0} is : the category {1} and costs {2} per unit.",
              p.ProductName, p.Category.CategoryName, p.UnitPrice);
  }
  
  /* Select - Indexed
     
  public void Linq12()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      var numsInPlace = numbers.Select((num, index) => new { Num = num, InPlace = (num == index) });
  
      Console.WriteLine("Number: In-place?");
      foreach (var n in numsInPlace)
      {
          Console.WriteLine("{0}: {1}", n.Num, n.InPlace);
      }
  }
   */
  
  public void Batch12() {
    print("***** Batch12");
  
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
    print("Number: In-place?");
    int index = 0;
    for (int n : numbers) {
      print("{0}: {1}", n, (n == index));
      index = index + 1;
    }
  }
  
  /* Select - Filtered
   
   public void Linq13()
    {
        int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
        string[] digits = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
    
        var lowNums =
            from n in numbers
            where n < 5
            select digits[n];
    
        Console.WriteLine("Numbers < 5:");
        foreach (var num in lowNums)
        {
            Console.WriteLine(num);
        }
  }
*/
  
  public void Batch13() {
    print("***** Batch13");  
  
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    String[] digits = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
  
    print("Numbers < 5:");
    for (int n : numbers)
      if (n < 5)
        print(digits[n]);
  }
  
  /* SelectMany - Compound from 1
   public void Linq14()
  {
      int[] numbersA = { 0, 2, 4, 5, 6, 8, 9 };
      int[] numbersB = { 1, 3, 5, 7, 8 };
  
      var pairs =
          from a in numbersA
          from b in numbersB
          where a < b
          select new { a, b };
  
      Console.WriteLine("Pairs where a < b:");
      foreach (var pair in pairs)
      {
          Console.WriteLine("{0} is less than {1}", pair.a, pair.b);
      }
  }
   
   */
  
  public void Batch14() {
    print("***** Batch14");
  
    int[] numbersA = {0, 2, 4, 5, 6, 8, 9};
    int[] numbersB = {1, 3, 5, 7, 8};
  
    print("Pairs where a < b:");
    for (int a : numbersA)
      for (int b : numbersB)
        if (a < b)
          print("{0} is less than {1}", a, b);
  }
  
  /*
   * SelectMany - Compound from 2
   * 
   * public void Batch15() { List<Customer> customers = GetCustomerList();
   * 
   * var orders = from c in customers from o in c.Orders where o.Total < 500.00M
   * select new { c.CustomerID, o.OrderID, o.Total };
   * 
   * ObjectDumper.Write(orders); }
   */
  public void Batch15() {
    print("***** Batch15");
    for (Northwind db : connection)
      for (Customer c : db.Customers)
        for (Order o : c.Orders) {
          double total = o.OrderDetails.dsum(Order_Details.ByTotal);
          if (total < 500)
            print("Customer={0} Order={1} Total={2}", 
                o.Customer.CustomerID, o.OrderID, total);
        }
  }
  
  /*
   * SelectMany - Compound from 3
   * 
   * public void Batch16() { List<Customer> customers = GetCustomerList();
   * 
   * var orders = from c in customers from o in c.Orders where o.OrderDate >= new
   * DateTime(1998, 1, 1) select new { c.CustomerID, o.OrderID, o.OrderDate };
   * 
   * ObjectDumper.Write(orders); }
   */
  public void Batch16() {
    print("***** Batch16");
    for (Northwind db : connection)
      for (Customer c : db.Customers)
        for (Order o : c.Orders)
          if (o.OrderDate.after(Date.valueOf("1998-01-01")))
            print("Customer={0} Order={1} Date={2}", 
                c.CustomerID, o.OrderID, o.OrderDate);
  }
  
  /*
   * SelectMany - from Assignment [fixed]
   * 
   * public void Batch17() { List<Customer> customers = GetCustomerList();
   * 
   * var orders = from c in customers from o in c.Orders from total = o.Total
   * where total >= 2000.0M select new { c.CustomerID, o.OrderID, total };
   * 
   * ObjectDumper.Write(orders); }
   */
  public void Batch17() {
    print("***** Batch17");
    for (Northwind db : connection)
      for (Customer c : db.Customers)
        for (Order o : c.Orders) {
          double total = o.OrderDetails.dsum(Order_Details.ByTotal);
          if (total > 2000)
            print("Customer={0} Order={1} Total={2}", 
                c.CustomerID, o.OrderID, total);
        }
  }
  
  /*
   * SelectMany - Multiple from
   * 
   * public void Batch18() { List<Customer> customers = GetCustomerList();
   * 
   * DateTime cutoffDate = new DateTime(1997, 1, 1);
   * 
   * var orders = from c in customers where c.Region == "WA" from o in c.Orders
   * where o.OrderDate >= cutoffDate select new { c.CustomerID, o.OrderID };
   * 
   * ObjectDumper.Write(orders); }
   */
  public void Batch18() {
    print("***** Batch18");
    Date cutoffDate = Date.valueOf("1997-01-01");
    for (Northwind db : connection)
     for (Customer c : db.Customers)
      if (c.Region == "WA") {
       print("Customer {0}:", c.CompanyName);
       for (Order o : c.Orders)
        if (o.OrderDate.after(cutoffDate))
         print("  {0}", o.OrderDate);
      }
  }
  
  /*
   * SelectMany - Indexed
   * 
   * public void Batch19() { List<Customer> customers = GetCustomerList();
   * 
   * var customerOrders = customers.SelectMany( (cust, custIndex) =>
   * cust.Orders.Select(o => "Customer #" + (custIndex + 1) +
   * " has an order with OrderID " + o.OrderID));
   * 
   * ObjectDumper.Write(customerOrders); }
   */
  public void Batch19() {
    print("***** Batch19");
    int index = 0;
    for (Northwind db : connection)
      for (Customer cust : db.Customers) {
        index = index + 1;
        for (Order o : cust.Orders)
          print("Customer #{0} has an order with OrderID {1}", index, o.OrderID);
      }
  }
  
  /* done */
  
  public void test() throws SQLException {
    Batch6();
    Batch7();
    Batch8();
    Batch9();
    Batch10();
    Batch11();
    Batch12();
    Batch13();
    Batch14();
    Batch15();
    Batch16();
    Batch17();
    Batch18();
    Batch19();
  }

}
