package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.schema.Category;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Product;

public class LINQQuantifier extends BaseTest {
  public static void main(String[] args) throws SQLException {
    new LINQQuantifier().run();
  }

  /*
   Any - Simple
  This sample uses Any to determine if any of the words in the array contain the substring 'ei'.
  private void Linq67()
  {
      string[] words = { "believe", "relief", "receipt", "field" };
      bool iAfterE = words.Any(w => w.Contains("ei"));
      Console.WriteLine("There is a word that contains in the list that contains 'ei': {0}", iAfterE);
  }
  Result
  There is a word that contains in the list that contains 'ei': True

  Any - Grouped
  This sample uses Any to return a grouped a list of products only for categories that have at least one product that is out of stock.
  private void Linq69()
  {
      List<Product> products = GetProductList();
      var productGroups =
          from p in products
          group p by p.Category into g
          where g.Any(p => p.UnitsInStock == 0)
          select new { Category = g.Key, Products = g };
      ObjectDumper.Write(productGroups, 1);
  }
  Result
  Category=Condiments     Products=...
    Products: ProductID=3   ProductName=Aniseed Syrup       Category=Condiments     UnitPrice=10.0000       UnitsInStock=13
  */
  private void Batch69() {
    print("Update products:");
    for (Northwind db : connection) {
      for (Category c : db.Categories)
        if (c.Products.exists(Product.isOutOfStock)) {
          print("Category={0} has out out-of-stock product", c.CategoryName);
          for (Product p : c.Products)
            print("  Product {0}", p.ProductName);
        }
    }
  }

  /*
  All - Simple
  This sample uses All to determine whether an array contains only odd numbers.
  private void Linq70()
  {
  int[] numbers = { 1, 11, 3, 19, 41, 65, 19 };
  bool onlyOdd = numbers.All(n => n % 2 == 1);
  Console.WriteLine("The list contains only odd numbers: {0}", onlyOdd);
  }
  Result
  The list contains only odd numbers: True
  All - Grouped
  This sample uses All to return a grouped a list of products only for categories that have all of their products in stock.
  private void Linq72()
  {
  List<Product> products = GetProductList();
  var productGroups =
      from p in products
      group p by p.Category into g
      where g.All(p => p.UnitsInStock > 0)
      select new { Category = g.Key, Products = g };
  ObjectDumper.Write(productGroups, 1);
  }
  */
  public void test() throws SQLException {
    Batch69();
  }
}
