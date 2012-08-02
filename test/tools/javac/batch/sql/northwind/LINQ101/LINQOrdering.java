package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Product;
import batch.Fun;
import batch.sql.Many;
import batch.util.MemSet;

public class LINQOrdering extends BaseTest {
	public static void main(String[] args) throws SQLException {
		new LINQOrdering().run();
	}
	
	/* OrderBy - Simple 1
	 
	 public void Linq28()
  	{
  	    string[] words = { "cherry", "apple", "blueberry" };
  
  	    var sortedWords =
  	        from w in words
  	        orderby w
  	        select w;
  
  	    Console.WriteLine("The sorted list of words:");
  	    foreach (var w in sortedWords)
  	    {
  	        Console.WriteLine(w);
  	    }
  	}
  */
	
	public void Batch28() {
	  print("***** Batch28");
	  Many<String> words = MemSet.make("cherry", "apple", "blueberry");
	  print("The sorted list of words:");
	  Fun<String, String> id = new Fun<String, String>(){ public String apply(String s) {return s;}}; 
	  for (String word : words.orderBy(id, true))
	    print("{0}", word);
	  
	}
	
	/* OrderBy - Simple 2
  
  public void Linq29()
  {
      string[] words = { "cherry", "apple", "blueberry" };
  
      var sortedWords =
          from w in words
          orderby w.Length
          select w;
  
      Console.WriteLine("The sorted list of words (by length):");
      foreach (var w in sortedWords)
      {
          Console.WriteLine(w);
      }
  }
 */
 
 public void Batch29() {
   print("***** Batch29");
   Many<String> words = MemSet.make("cherry", "apple", "blueberry");
   print("The sorted list of words (by length):");
   Fun<String, Integer> lengthF = new Fun<String, Integer>(){ public Integer apply(String s) {return s.length();}}; 
   for (String word : words.orderBy(lengthF, true))
     print("{0}", word);
   
 }

	/* OrderBy - Simple 3

	public void Linq30()
  {
      List<Product> products = GetProductList();
  
      var sortedProducts =
          from p in products
          orderby p.ProductName
          select p;
  
      ObjectDumper.Write(sortedProducts);
  }
	*/
	public void Batch30() {
		print("***** Batch30");
		for (Northwind db : connection)
			for (Product p : db.Products.orderBy(Product.byName, true))
				print("Name={0} UnitPrice={1}", p.ProductName, p.UnitPrice);
	}
	
	/* OrderBy - Comparer
	 public void Linq31()
    {
        string[] words = { "aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry" };
    
        var sortedWords = words.OrderBy(a => a, new CaseInsensitiveComparer());
    
        ObjectDumper.Write(sortedWords);
    }
    
    public class CaseInsensitiveComparer : IComparer<string>
    {
        public int Compare(string x, string y)
        {
            return string.Compare(x, y, StringComparison.OrdinalIgnoreCase);
        }
    }
	 */
	
	public void Batch31() {
	  print("***** Batch31");
    Many<String> words = MemSet.make("aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry");
	  Fun<String, String> caseInsensitive = new Fun<String, String>(){ 
	      public String apply(String s) {return s.toLowerCase();}
	    }; 
	  for (String word : words.orderBy(caseInsensitive, true)) 
	    print("{0}", word);
	}
	
	/* OrderByDescending - Simple 1
  public void Linq32()
  {
      double[] doubles = { 1.7, 2.3, 1.9, 4.1, 2.9 };
  
      var sortedDoubles =
          from d in doubles
          orderby d descending
          select d;
  
      Console.WriteLine("The doubles from highest to lowest:");
      foreach (var d in sortedDoubles)
      {
          Console.WriteLine(d);
      }
  }
  */
	public void Batch32() {
	  print("***** Batch32");
    Many<Double> doubles = MemSet.make(1.7, 2.3, 1.9, 4.1, 2.9);
    print("The doubles from highest to lowest:");
    Fun<Double, Double> id = new Fun<Double, Double>(){ public Double apply(Double d) {return d;}}; 
    for (Double d : doubles.orderBy(id, false))
      print("{0}", d);
    
	}

	/* OrderByDescending - Simple 2

	public void Linq33()
  {
      List<Product> products = GetProductList();
  
      var sortedProducts =
          from p in products
          orderby p.UnitsInStock descending
          select p;
  
      ObjectDumper.Write(sortedProducts);
  }
	*/
	public void Batch33() {
		print("***** Batch33");
		for (Northwind db : connection)
			for (Product p : db.Products.orderBy(Product.byUnitsInStock, false))
				print("Name={0} UnitPrice={1}", p.ProductName, p.UnitsInStock);
	}
	
	/* OrderByDescending - Comparer
  public void Linq34()
  {
      string[] words = { "aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry" };
  
      var sortedWords = words.OrderByDescending(a => a, new CaseInsensitiveComparer());
  
      ObjectDumper.Write(sortedWords);
  }
  
  public class CaseInsensitiveComparer : IComparer<string>
  {
      public int Compare(string x, string y)
      {
          return string.Compare(x, y, StringComparison.OrdinalIgnoreCase);
      }
  }
  */
 
 public void Batch34() {
   print("***** Batch34");
   Many<String> words = MemSet.make("aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry");
   Fun<String, String> caseInsensitive = new Fun<String, String>(){ 
       public String apply(String s) {return s.toLowerCase();}
     }; 
   for (String word : words.orderBy(caseInsensitive, false)) 
     print("{0}", word);
 }
	

 /* ThenBy - Simple 1
 public void Linq35()
  {
      string[] digits = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
  
      var sortedDigits =
          from d in digits
          orderby d.Length, d
          select d;
  
      Console.WriteLine("Sorted digits:");
      foreach (var d in sortedDigits)
      {
          Console.WriteLine(d);
      }
  }
 */
 public void Batch35() {
   print("***** Batch35");
   Many<String> digits = MemSet.make("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
   print("Sorted digits:");
   Fun<String, String> lengthIdF = new Fun<String, String>() { 
     public String apply(String s) {
       return s.length() + s;
     }}; 
   for (String d : digits.orderBy(lengthIdF, true))
     print("{0}", d);
   
 }
 
 /* ThenBy - Comparator
   public void Linq36()
  {
      string[] words = { "aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry" };
  
      var sortedWords =
          words.OrderBy(a => a.Length)
               .ThenBy(a => a, new CaseInsensitiveComparer());
  
      ObjectDumper.Write(sortedWords);
  }
  
  public class CaseInsensitiveComparer : IComparer<string>
  {
      public int Compare(string x, string y)
      {
          return string.Compare(x, y, StringComparison.OrdinalIgnoreCase);
      }
  }
 */
 public void Batch36() {
   print("***** Batch36");
   Many<String> words = MemSet.make("aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry");
   Fun<String, String> caseInsensitive = new Fun<String, String>(){ 
       public String apply(String s) {return s.length() + s.toLowerCase();}
     }; 
   for (String word : words.orderBy(caseInsensitive, true)) 
     print("{0}", word);
   
 }
 
	/* ThenByDescending - Simple

	public void Linq37()
  {
      List<Product> products = GetProductList();
  
      var sortedProducts =
          from p in products
          orderby p.Category, p.UnitPrice descending
          select p;
  
      ObjectDumper.Write(sortedProducts);
  }
	*/

	public void Batch37() {
		print("***** Batch37");
		for (Northwind db : connection)
			for (Product p : db.Products.orderBy(Product.byCategoryName, true)
					.orderBy(Product.byUnitPrice, false))
				print("Category={0}\tUnitPrice={1}\tProductName={2}",
						p.Category.CategoryName,
						p.UnitPrice,
						p.ProductName);
	}
	
	/* ThenByDescending - Comparator
  public void Linq38()
  {
      string[] words = { "aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry" };
  
      var sortedWords =
          words.OrderBy(a => a.Length)
               .ThenByDescending(a => a, new CaseInsensitiveComparer());
  
      ObjectDumper.Write(sortedWords);
  }
  
  public class CaseInsensitiveComparer : IComparer<string>
  {
      public int Compare(string x, string y)
      {
          return string.Compare(x, y, StringComparison.OrdinalIgnoreCase);
      }
  }
  
  I THINK THERE'S AN ERROR IN THE LINQ EXAMPLE OUTPUT ON THEIR WEB PAGE
*/
  public void Batch38() {
    print("***** Batch38");
    Many<String> words = MemSet.make("aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry");
    Fun<String, String> caseInsensitive = new Fun<String, String>(){ 
        public String apply(String s) {return s.length() + s.toLowerCase();}
      }; 
    for (String word : words.orderBy(caseInsensitive, false)) 
      print("{0}", word);
    
  }
  
  /* Reverse
  public void Linq39()
  {
      string[] digits = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
  
      var reversedIDigits = (
          from d in digits
          where d[1] == 'i'
          select d)
          .Reverse();
  
      Console.WriteLine("A backwards list of the digits with a second character of 'i':");
      foreach (var d in reversedIDigits)
      {
          Console.WriteLine(d);
      }
  }
  */
  public void Batch39() {
    print("***** Batch38");
    
    String[] digits = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
    
    for (int i=digits.length-1; i>=0; i--)
      if (digits[i].charAt(1) == 'i')
        print("{0}", digits[i]);
  }

	public void test() throws SQLException {
		Batch28();
		Batch29();
	  Batch30();
	  Batch31();
	  Batch32();
	  Batch33();
	  Batch34();
	  Batch35();
	  Batch36();
		Batch37();
	  Batch38();
	  Batch39();
	}
}
