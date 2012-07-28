package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Product;

public class LINQElement extends BaseTest {
	public static void main(String[] args) throws SQLException {
		new LINQElement().run();
	}
	
	/*First - Simple
  
  public void Linq58()
  {
      List<Product> products = GetProductList();
  
      Product product12 = (
          from p in products
          where p.ProductID == 12
          select p)
          .First();
  
      ObjectDumper.Write(product12);
  
  }*/
	
	public void Batch58() {
	  print("***** Batch58");
	  
	  for (Northwind db : connection) {
	    Product p = db.Products.id(12);
	    print("ProductID={0} ProductName={1}", p.ProductID, p.ProductName);
	  }
	   
	}
	
	/* First - Condition 
	 public void Linq59()
    {
      string[] strings = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
      string startsWithO = strings.First(s => s[0] == 'o');
      Console.WriteLine("A string starting with 'o': {0}", startsWithO);
    }
	*/
	
	public void Batch59() {
    print("***** Batch59");
    String[] strings = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
    
    for (String s : strings) {
      if (s.charAt(0) == 'o') {
        print("A string starting with 'o': {0}", s);
        break;
      }        
    }
     
  }
	
	/* FirstOrDefault - Simple
	public void Linq61()
  {
      int[] numbers = { };
      int firstNumOrDefault = numbers.FirstOrDefault();
      Console.WriteLine(firstNumOrDefault);
  }
	 */
	
	// TODO: Java doesn't have default values
	
	/* FirstOrDefault - Condition
  public void Linq62()
  {
     List<Product> products = GetProductList();
     Product product789 = products.FirstOrDefault(p => p.ProductID == 789);
     Console.WriteLine("Product 789 exists: {0}", product789 != null);
  }
   */
	/*public void Batch62() {
    print("***** Batch62");
    
    for (Northwind db : connection) {
      Fun<Product, Boolean> isProduct789 = new Fun<Product, Boolean>() {
        public Boolean apply(Product p) {
          return p.ProductID == 789;
        }
      };
      Boolean exists = db.Products.exists(isProduct789);
      print("Product 789 exists: {0}", exists);
    }
	}*/
	
	/* ElementAt
	public void Linq64()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      int fourthLowNum = (
          from n in numbers
          where n > 5
          select n)
          .ElementAt(1);  // second number is index 1 because sequences use 0-based indexing
 
      Console.WriteLine("Second number > 5: {0}", fourthLowNum);
  }
	*/
	
	public void Batch64() {
	  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
	  boolean found = false;
	  for (int n : numbers)
	    if (n > 5)
	      if (found) {
	        print("Second number > 5: {0}", n);
	        break;
	      } else {
	        found = true;
	      }
	}
  
	public void test() throws SQLException {
		Batch58();
		Batch59();
		//Batch62();
		Batch64();
	}
}
