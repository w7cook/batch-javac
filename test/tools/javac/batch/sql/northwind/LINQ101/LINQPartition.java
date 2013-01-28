package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.schema.Customer;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Order;

public class LINQPartition extends BaseTest {
  public static void main(String[] args) throws SQLException {
    new LINQPartition().run();
  }

  /*
   * Take - Simple

    public void Linq20()
    {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
      var first3Numbers = numbers.Take(3);
    
      Console.WriteLine("First 3 numbers:");
      foreach (var n in first3Numbers)
      {
        Console.WriteLine(n);
      }
    }
   */
  private void Batch20() {
    print("***** Batch20");
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    for (int i = 0; i < 3; i++)
      print("{0}", numbers[i]);
  }

  /* Take - Nested

  private void Linq21()
  {
      List<Customer> customers = GetCustomerList();
      var first3WAOrders = (
          from c in customers
          from o in c.Orders
          where c.Region == "WA"
          select new { c.CustomerID, o.OrderID, o.OrderDate })
          .Take(3);
      Console.WriteLine("First 3 orders in WA:");
      foreach (var order in first3WAOrders)
      {
          ObjectDumper.Write(order);
      }
  }
  */
  private void Batch21() {
    print("***** Batch21");
    print("First 3 orders in WA:");
    for (Northwind db : connection)
      for (Order o : db.Orders.first(3))
        if (o.Customer.Region == "WA")
          print("CustomerID={0} OrderID={1} OrderDate={2}",
              o.Customer.CustomerID, o.OrderID, o.OrderDate);
  }

  /* Skip - Simple
    public void Linq22()
    {
        int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
        var allButFirst4Numbers = numbers.Skip(4);
        Console.WriteLine("All but first 4 numbers:");
    
        foreach (var n in allButFirst4Numbers)
        {
            Console.WriteLine(n);
        }
    }
   */
  private void Batch22() {
    print("***** Batch22");
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    print("All but first 4 numbers:");
    for (int i = 4; i < numbers.length; i++)
      print("{0}", numbers[i]);
  }

  /* Skip - Nested

  private void Linq23()
  {
  	List<Customer> customers = GetCustomerList();
  	var waOrders =
  	    from c in customers
  	    from o in c.Orders
  	    where c.Region == "WA"
  	    select new { c.CustomerID, o.OrderID, o.OrderDate };
  	var allButFirst2Orders = waOrders.Skip(2);
  	Console.WriteLine("All but first 2 orders in WA:");
  	foreach (var order in allButFirst2Orders)
  	{
  	    ObjectDumper.Write(order);
  	}
  }
  */

  private void Batch23() {
    print("***** Batch22");
    // Note: standard SQL does not support skipping rows at the beginning of a query
    int index = 0;
    for (Northwind db : connection) {
      print("All but first 2 orders in WA:");
      for (Customer c : db.Customers)
        if (c.Region == "WA")
          for (Order o : c.Orders)
            if (index > 1)
              print("CustomerID={0} OrderID={1} OrderDate={2}",
                  o.Customer.CustomerID, o.OrderID, o.OrderDate);
            else
              index = index + 1;
    }
  }

  /* TakeWhile - Simple
  public void Linq24()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      var firstNumbersLessThan6 = numbers.TakeWhile(n => n < 6);
  
      Console.WriteLine("First numbers less than 6:");
  
      foreach (var n in firstNumbersLessThan6)
      {
          Console.WriteLine(n);
      }
  }
  */
  private void Batch24() {
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    print("***** Batch24");
    print("First numbers less than 6:");
    for (int i = 0; i < numbers.length; i++) {
      if (numbers[i] > 6)
        break;
      print("{0}", numbers[i]);
    }
  }

  /* TakeWhile - Indexed
  public void Linq25()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      var firstSmallNumbers = numbers.TakeWhile((n, index) => n >= index);
  
       Console.WriteLine("First numbers not less than their position:");
  
      foreach (var n in firstSmallNumbers)
      {
          Console.WriteLine(n);
      }
  }
  */
  private void Batch25() {
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    print("***** Batch25");
    print("First numbers not less than their position:");
    for (int i = 0; i < numbers.length; i++) {
      if (numbers[i] < i)
        break;
      print("{0}", numbers[i]);
    }
  }

  /* SkipWhile - Simple
  public void Linq26()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      var allButFirst3Numbers = numbers.SkipWhile(n => n % 3 != 0);

      Console.WriteLine("All elements starting from first element divisible by 3:");

      foreach (var n in allButFirst3Numbers)
      {
          Console.WriteLine(n);
      }
  }
  */
  private void Batch26() {
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    print("***** Batch26");
    print("All elements starting from first element divisible by 3:");
    int i = 0;
    while (numbers[i] % 3 != 0)
      i++;
    for (; i < numbers.length; i++) {
      print("{0}", numbers[i]);
    }
  }

  /* TakeWhile - Indexed
  public void Linq27()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      var laterNumbers = numbers.SkipWhile((n, index) => n >= index);
  
      Console.WriteLine("All elements starting from first element less than its position:");
  
      foreach (var n in laterNumbers)
      {
          Console.WriteLine(n);
      }
  }
  */
  private void Batch27() {
    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
    print("***** Batch27");
    print("All elements starting from first element less than its position:");
    int i = 0;
    while (numbers[i] >= i)
      i++;
    for (; i < numbers.length; i++) {
      print("{0}", numbers[i]);
    }
  }

  public void test() throws SQLException {
    Batch20();
    Batch21();
    Batch22();
    Batch23();
    Batch24();
    Batch25();
    Batch26();
    Batch27();
  }

}
