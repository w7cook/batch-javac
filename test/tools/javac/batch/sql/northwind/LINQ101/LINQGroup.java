package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.schema.Category;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Order;
import sql.northwind.schema.Product;
import batch.Fun;
import batch.sql.Group;
import batch.sql.Many;
import batch.util.MemSet;
import java.util.Arrays;

public class LINQGroup extends BaseTest {
	public static void main(String[] args) throws SQLException {
		new LINQGroup().run();
	}
	/*GroupBy - Simple 1

	public void Linq40()
	{
	    int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };

	    var numberGroups =
	        from n in numbers
	        group n by n % 5 into g
	        select new { Remainder = g.Key, Numbers = g };

	    foreach (var g in numberGroups)
	    {
	        Console.WriteLine("Numbers with a remainder of {0} when divided by 5:", g.Remainder);
	        foreach (var n in g.Numbers)
	        {
	            Console.WriteLine(n);
	        }
	    }
	}*/
	public void Batch40() {
	  print("***** Batch40");
    
	  Many<Integer> numbers = MemSet.make(5, 4, 1, 3, 9, 8, 6, 7, 2, 0);
	  Fun<Integer, Integer> remainderF = new Fun<Integer, Integer>() {public Integer apply(Integer i) {return i%5;}};
	  
	  for (Group<Integer, Integer> g : numbers.groupBy(remainderF)) {
	    print("Numbers with remainder of {0} when divided by 5", g.Key);
	    for (Integer n : g.Items)
	      print("{0}", n);
	  }
	}
	
	
	/*GroupBy - Simple 2

	public void Linq41()
	{
	    string[] words = { "blueberry", "chimpanzee", "abacus", "banana", "apple", "cheese" };

	    var wordGroups =
	        from w in words
	        group w by w[0] into g
	        select new { FirstLetter = g.Key, Words = g };

	    foreach (var g in wordGroups)
	    {
	        Console.WriteLine("Words that start with the letter '{0}':", g.FirstLetter);
	        foreach (var w in g.Words)
	        {
	            Console.WriteLine(w);
	        }
	    }
	}*/
	public void Batch41() {
    print("***** Batch41");
    
    Many<String> numbers = MemSet.make("blueberry", "chimpanzee", "abacus", "banana", "apple", "cheese");
    Fun<String, String> firstCharF = new Fun<String, String>() {public String apply(String s) {return s.substring(0, 1);}};
     
    for (Group<String, String> g : numbers.groupBy(firstCharF)) {
      print("Words that start with the letter '{0}':", g.Key);
      for (String s : g.Items)
        print("{0}", s);
    }
  }

	/*
	public void Linq42()
	{
	    List<Product> products = GetProductList();

	    var orderGroups =
	        from p in products
	        group p by p.Category into g
	        select new { Category = g.Key, Products = g };

	    ObjectDumper.Write(orderGroups, 1);
	}

	*/
	public void Batch42() {
	  print("***** Batch42");
    for (Northwind db : connection) {
			for (Group<Category, Product> g : db.Products.groupBy(Product.byCategory)) {
				print("Category={0}", g.Key.CategoryName);
				for (Product p : g.Items)
					print("  Products: ProductName={0} UnitPrice={1} UnitsInStock={0}",
							p.ProductName,
							p.UnitPrice,
							p.UnitsInStock);
			}
		}
	}

	/*
	public void Linq43()
	{
	    List<Customer> customers = GetCustomerList();

	    var customerOrderGroups =
	        from c in customers
	        select
	            new
	            {
	                c.CompanyName,
	                YearGroups =
	                    from o in c.Orders
	                    group o by o.OrderDate.Year into yg
	                    select
	                        new
	                        {
	                            Year = yg.Key,
	                            MonthGroups =
	                                from o in yg
	                                group o by o.OrderDate.Month into mg
	                                select new { Month = mg.Key, Orders = mg }
	                        }
	            };

	    ObjectDumper.Write(customerOrderGroups, 3);
	}*/

	public void Batch43() {
		print("GroupBy - Nested");
		for (Northwind db : connection)
				for (Group<Long, Order> YearGroup : db.Orders.groupBy(Order.Year)) {
					print(" Year={0}", YearGroup.Key);
					for (Order o : YearGroup.Items)
						print("    OrderDate={0} Freight={1}",
								o.OrderDate,
								o.Freight);
				}
	}

	/*
	GroupBy - Comparer

	This sample uses GroupBy to partition trimmed elements of an array using a custom comparer that matches words that are anagrams of each other.

	public void Linq44()
	{
	    string[] anagrams = { "from   ", " salt", " earn ", "  last   ", " near ", " form  " };

	    var orderGroups = anagrams.GroupBy(w => w.Trim(), new AnagramEqualityComparer());

	    ObjectDumper.Write(orderGroups, 1);
	}

	public class AnagramEqualityComparer : IEqualityComparer<string>
	{
	    public bool Equals(string x, string y)
	    {
	        return getCanonicalString(x) == getCanonicalString(y);
	    }

	    public int GetHashCode(string obj)
	    {
	        return getCanonicalString(obj).GetHashCode();
	    }

	    private string getCanonicalString(string word)
	    {
	        char[] wordChars = word.ToCharArray();
	        Array.`<char>(wordChars);
	        return new string(wordChars);
	    }
	}*/
	
	public void Batch44() {
    print("***** Batch44");
    
    Many<String> anagrams = MemSet.make("from   ", " salt", " earn ", "  last   ", " near ", " form  ");
    Fun<String, String> canonicalF = new Fun<String, String>() {
      public String apply(String s) {
        // TODO: not possible to express in batches? 
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars).trim();
      }
    };
    
    for (Group<String, String> g : anagrams.groupBy(canonicalF)) {
      for (String s : g.Items)
        print("{0}", s);
    }
  }
	
	/*GroupBy - Comparer, Mapped

	This sample uses GroupBy to partition trimmed elements of an array using a custom comparer that matches words that are anagrams of each other, and then converts the results to uppercase.

	public void Linq45()
	{
	    string[] anagrams = { "from   ", " salt", " earn ", "  last   ", " near ", " form  " };

	    var orderGroups = anagrams.GroupBy(
	                w => w.Trim(),
	                a => a.ToUpper(),
	                new AnagramEqualityComparer()
	                );

	    ObjectDumper.Write(orderGroups, 1);
	}

	public class AnagramEqualityComparer : IEqualityComparer<string>
	{
	    public bool Equals(string x, string y)
	    {
	        return getCanonicalString(x) == getCanonicalString(y);
	    }

	    public int GetHashCode(string obj)
	    {
	        return getCanonicalString(obj).GetHashCode();
	    }

	    private string getCanonicalString(string word)
	    {
	        char[] wordChars = word.ToCharArray();
	        Array.Sort<char>(wordChars);
	        return new string(wordChars);
	    }
	} */
	
	public void Batch45() {
    print("***** Batch45");
    
    Many<String> anagrams = MemSet.make("from   ", " salt", " earn ", "  last   ", " near ", " form  ");
    Fun<String, String> canonicalF = new Fun<String, String>() {
      public String apply(String s) {
        // TODO: not possible to express in batches? 
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars).trim().toUpperCase();
      }
    };
    
    for (Group<String, String> g : anagrams.groupBy(canonicalF)) {
      for (String s : g.Items)
        print("{0}", s);
    }
  }
	
	public void test() throws SQLException {
	  Batch40();
	  Batch41();
	  Batch42();
	  Batch43();
	  Batch44();
	  Batch45();
	}
}
