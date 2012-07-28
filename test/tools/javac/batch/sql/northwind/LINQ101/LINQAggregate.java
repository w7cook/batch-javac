package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.schema.Category;
import sql.northwind.schema.Customer;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Product;
import batch.Fun;
import batch.sql.Group;
import batch.sql.Many;
import batch.util.MemSet;

public class LINQAggregate extends BaseTest {
	public static void main(String[] args) throws SQLException {
		new LINQAggregate().run();
	}
	
	/* Count - Simple
  public void Linq73()
  {
      int[] factorsOf300 = { 2, 2, 3, 5, 5 };
  
      int uniqueFactors = factorsOf300.Distinct().Count();
  
      Console.WriteLine("There are {0} unique factors of 300.", uniqueFactors);
  }
  */
	
	public void Batch73() {
	  print("***** Batch73");
    Many<Integer> factorsOf300 = MemSet.make(2, 2, 3, 5, 5);
    int uniqueFactors = factorsOf300.distinct().count();
	  print("There are {0} unique factors of 300.", uniqueFactors);
	}
	
	/* Count - Conditional
	public void Linq74()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      int oddNumbers = numbers.Count(n => n % 2 == 1);
  
      Console.WriteLine("There are {0} odd numbers in the list.", oddNumbers);
  } 
	*/
	
	public void Batch74() {
    print("***** Batch74");
    Many<Integer> numbers = MemSet.make(5, 4, 1, 3, 9, 8, 6, 7, 2, 0);
    Fun<Integer, Boolean> oddF = new Fun<Integer, Boolean>() {public Boolean apply(Integer i) {return i%2==0;}};
    int oddNumbers = numbers.count(oddF);
    print("There are {0} odd numbers in the list.", oddNumbers);
  }
	
	/* Count - Nested

	private void Linq76() {
	   List customers = GetCustomerList();

	   var orderCounts =
	      from c in customers
	      select new {c.CustomerID, OrderCount = c.Orders.Count()};

	   ObjectDumper.Write(orderCounts);
	}
	*/

	public void Batch76() {
		print("***** Batch76");
		for (Northwind db : connection)
			for (Customer c : db.Customers)
				print("CustomerID={0}\t OrderCount={1}", c.CustomerID, c.Orders.count());
	}

	/* Count - Grouped
	
	private void Linq77() {
	   List products = GetProductList();

	   var categoryCounts =
	      from p in products
	      group p by p.Category into g
	      select new {Category = g.Key, 
	          ProductCount = g.Group.Count()};

	   ObjectDumper.Write(categoryCounts);
	}
	*/

	public void Batch77() {
		print("***** Batch77");
		for (Northwind db : connection)
			for (Group<Category, Product> g : db.Products.groupBy(Product.byCategory))
				print("Category={0}\t ProductCount={1}",
						g.Key.CategoryName,
						g.Items.count());
	}
	
	/* Sum - Simple
	 public void Linq78()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      double numSum = numbers.Sum();
  
      Console.WriteLine("The sum of the numbers is {0}.", numSum);
  }
	*/
	
	public void Batch78() {
	  print("***** Batch78");
    Many<Integer> numbers = MemSet.make(5, 4, 1, 3, 9, 8, 6, 7, 2, 0);
    Fun<Integer, Long> sumID = new Fun<Integer, Long>() {public Long apply(Integer i) {return i.longValue();}};
    long numSum = numbers.sum(sumID);
    print("The sum of the numbers is {0}.", numSum);
	}

	/* Sum - Projection
	public void Linq79()
  {
      string[] words = { "cherry", "apple", "blueberry" };
  
      double totalChars = words.Sum(w => w.Length);
  
      Console.WriteLine("There are a total of {0} characters in these words.", totalChars);
  }
	 */
	
	public void Batch79() {
	  print("***** Batch79");
    Many<String> words = MemSet.make("cherry", "apple", "blueberry");
    Fun<String, Integer> lengthF = new Fun<String, Integer>() {public Integer apply(String s) {return s.length();}};
    Fun<Integer, Long> longF = new Fun<Integer, Long>() {public Long apply(Integer i) {return i.longValue();}};
    long totalChars = words.project(lengthF).sum(longF);
    print("There are a total of {0} characters in these words.", totalChars);
	}
	
	/* Sum - Grouped

	public void Linq80()
  {
      List<Product> products = GetProductList();
  
      var categories =
          from p in products
          group p by p.Category into g
          select new { Category = g.Key, TotalUnitsInStock = g.Sum(p => p.UnitsInStock) };
  
      ObjectDumper.Write(categories);
  }
	*/

	public void Batch80() {
		print("***** Batch80");
		for (Northwind db : connection)
			for (Group<Category, Product> g : db.Products.groupBy(Product.byCategory))
				print("Category={0}\t TotalUnitsInStock={1}",
						g.Key.CategoryName,
						g.Items.sum(Product.byUnitsInStock));
	}
	
	/* Min - Simple
	public void Linq81()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      int minNum = numbers.Min();
  
      Console.WriteLine("The minimum number is {0}.", minNum);
  }
	*/
	
	public void Batch81() {
    print("***** Batch81");
    Many<Integer> numbers = MemSet.make(5, 4, 1, 3, 9, 8, 6, 7, 2, 0);
    Fun<Integer, Long> longF = new Fun<Integer, Long>() {public Long apply(Integer i) {return i.longValue();}};
    long min = numbers.min(longF);
    print("The minimum number is {0}.", min);
  }
	
	/* Min - Projection
	public void Linq82()
  {
      string[] words = { "cherry", "apple", "blueberry" };
  
      int shortestWord = words.Min(w => w.Length);
  
      Console.WriteLine("The shortest word is {0} characters long.", shortestWord);
  }
	*/
	
	public void Batch82() {
    print("***** Batch82");
    Many<String> words = MemSet.make("cherry", "apple", "blueberry");
    Fun<String, Integer> lengthF = new Fun<String, Integer>() {public Integer apply(String s) {return s.length();}};
    Fun<Integer, Long> longF = new Fun<Integer, Long>() {public Long apply(Integer i) {return i.longValue();}};
    long shortestWord = words.project(lengthF).min(longF);
    print("The shortest word is {0} characters long.", shortestWord);
  }

	/* Min - Grouped

	public void Linq83()
  {
      List<Product> products = GetProductList();
  
      var categories =
          from p in products
          group p by p.Category into g
          select new { Category = g.Key, CheapestPrice = g.Min(p => p.UnitPrice) };
  
      ObjectDumper.Write(categories);
  }
	*/

	public void Batch83() {
		print("***** Batch83");
		for (Northwind db : connection)
			for (Group<Category, Product> g : db.Products.groupBy(Product.byCategory))
				print("Category={0}\t CheapestPrice={1}",
						g.Key.CategoryName,
						g.Items.dmin(Product.byUnitPrice));
	}

	/* Min - Elements

	public void Linq84()
  {
      List<Product> products = GetProductList();
  
      var categories =
          from p in products
          group p by p.Category into g
          let minPrice = g.Min(p => p.UnitPrice)
          select new { Category = g.Key, CheapestProducts = g.Where(p => p.UnitPrice == minPrice) };
  
      ObjectDumper.Write(categories, 1);
  }
	*/

	public void Batch84() {
		print("***** Batch84");
		for (Northwind db : connection)
			for (Group<Category, Product> g : db.Products.groupBy(Product.byCategory)) {
				print("Category {0}", g.Key.CategoryName);
				for (Product p : g.Items) {
				  double minPrice = g.Items.dmin(Product.byUnitPrice);
	        // TODO: BUG: Jaba generates an incorrect where clause: it matches
				  //            only the minimum price of ALL products. 
				  if (p.UnitPrice == minPrice)
						print("  Cheapest Products includes: {0}", p.ProductName);
				}
			}
	}
	
	/* Max - Simple
	public void Linq85()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      int maxNum = numbers.Max();
  
      Console.WriteLine("The maximum number is {0}.", maxNum);
  }
	*/
	
	public void Batch85() {
    print("***** Batch85");
    Many<Integer> numbers = MemSet.make(5, 4, 1, 3, 9, 8, 6, 7, 2, 0);
    Fun<Integer, Long> longF = new Fun<Integer, Long>() {public Long apply(Integer i) {return i.longValue();}};
    long max = numbers.max(longF);
    print("The maximum number is {0}.", max);
  }
	
	/* Max - Projection
	public void Linq86()
  {
      string[] words = { "cherry", "apple", "blueberry" };
  
      int longestLength = words.Max(w => w.Length);
  
      Console.WriteLine("The longest word is {0} characters long.", longestLength);
  }
	 */
	
	public void Batch86() {
    print("***** Batch86");
    Many<String> words = MemSet.make("cherry", "apple", "blueberry");
    Fun<String, Integer> lengthF = new Fun<String, Integer>() {public Integer apply(String s) {return s.length();}};
    Fun<Integer, Long> longF = new Fun<Integer, Long>() {public Long apply(Integer i) {return i.longValue();}};
    long longestLength = words.project(lengthF).max(longF);
    print("The longest word is {0} characters long.", longestLength);
  }

	/* Max - Grouped

	public void Linq87()
  {
      List<Product> products = GetProductList();
  
      var categories =
          from p in products
          group p by p.Category into g
          select new { Category = g.Key, MostExpensivePrice = g.Max(p => p.UnitPrice) };
  
      ObjectDumper.Write(categories);
  }
	*/

	public void Batch87() {
		print("***** Batch87");
		for (Northwind db : connection)
			for (Group<Category, Product> g : db.Products.groupBy(Product.byCategory))
				print("Category={0} MostExpensivePrice={1}",
						g.Key.CategoryName,
						g.Items.dmax(Product.byUnitPrice));
	}

	/* Max - Elements

	public void Linq88()
  {
      List<Product> products = GetProductList();
  
      var categories =
          from p in products
          group p by p.Category into g
          let maxPrice = g.Max(p => p.UnitPrice)
          select new { Category = g.Key, MostExpensiveProducts = g.Where(p => p.UnitPrice == maxPrice) };
  
      ObjectDumper.Write(categories, 1);
  }
	*/

	public void Batch88() {
		print("***** Batch88");
		for (Northwind db : connection)
			for (Group<Category, Product> g : db.Products.groupBy(Product.byCategory)) {
				print("Category {0}", g.Key.CategoryName);
				double maxPrice = g.Items.dmax(Product.byUnitPrice);
				for (Product p : g.Items)
				  // TODO: BUG: Jaba generates an incorrect where clause: it matches
          //            only the minimum price of ALL products. 
          if (p.UnitPrice == maxPrice)
						print("  Most Expensive Products includes: {0}", p.ProductName);
			}
	}
	

  /* Average - Simple
  
  public void Linq89()
  {
      int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  
      double averageNum = numbers.Average();
  
      Console.WriteLine("The average number is {0}.", averageNum);
  }
  */
  
  public void Batch89() {
    print("***** Batch89");
    Many<Integer> numbers = MemSet.make(5, 4, 1, 3, 9, 8, 6, 7, 2, 0);
    Fun<Integer, Double> doubleF = new Fun<Integer, Double>() {public Double apply(Integer i) {return i.doubleValue();}};
    double avg = numbers.average(doubleF);
    print("The average number is {0}.", avg);
  }
  
  /* Max - Projection
  public void Linq90()
  {
      string[] words = { "cherry", "apple", "blueberry" };
  
      double averageLength = words.Average(w => w.Length);
  
      Console.WriteLine("The average word length is {0} characters.", averageLength);
  }
  */
  
  public void Batch90() {
    print("***** Batch90");
    Many<String> words = MemSet.make("cherry", "apple", "blueberry");
    Fun<String, Integer> lengthF = new Fun<String, Integer>() {public Integer apply(String s) {return s.length();}};
    Fun<Integer, Double> doubleF = new Fun<Integer, Double>() {public Double apply(Integer i) {return i.doubleValue();}};
    double averageLength = words.project(lengthF).average(doubleF);
    print("The average word length is {0} characters.", averageLength);
  }

	/* Average - Grouped

	private void Linq91() {
	   List products = GetProductList();

	   var categories =
	      from p in products
	      group p by p.Category into g
	      select new {Category = g.Key, AveragePrice = g.Group.Average(p => p.UnitPrice)};

	   ObjectDumper.Write(categories);
	}
	*/
	public void Batch91() {
    print("***** Batch91");
    for (Northwind db : connection)
			for (Group<Category, Product> g : db.Products.groupBy(Product.byCategory))
				print("Category={0}\t AveragePrice={1}",
						g.Key.CategoryName,
						g.Items.average(Product.byUnitPrice));
	}
	
	/* Aggregate - Simple
	public void Linq92()
  {
      double[] doubles = { 1.7, 2.3, 1.9, 4.1, 2.9 };
  
      double product = doubles.Aggregate((runningProduct, nextFactor) => runningProduct * nextFactor);
  
      Console.WriteLine("Total product of all numbers: {0}", product);
  } 
	*/
	
	public void Batch92() {
	  print("***** Batch92");
/*    Many<Double> doubles = MemSet.make(1.7, 2.3, 1.9, 4.1, 2.9);
    double product = 1;
    for (double factor : doubles)
    	product *= factor;
    print("Total product of all numbers: {0}", product);
*/	}
	
	/* Aggregate - Seed
	public void Linq93()
  {
      double startBalance = 100.0;
  
      int[] attemptedWithdrawals = { 20, 10, 40, 50, 10, 70, 30 };
  
      double endBalance =
          attemptedWithdrawals.Aggregate(startBalance,
              (balance, nextWithdrawal) =>
                  ((nextWithdrawal <= balance) ? (balance - nextWithdrawal) : balance));
  
      Console.WriteLine("Ending balance: {0}", endBalance);
  }
	*/
	
	public void Batch93() {
    print("***** Batch93");
/*    int balance = 100;
    Many<Integer> attemptedWithdrawals = MemSet.make(20, 10, 40, 50, 10, 70, 30);
    for (int withdrawal : attemptedWithdrawals)
    	if (withdrawal <= balance)
    		balance -= withdrawal;
    print("Ending balance: {0}", balance);
*/
    }
	
	public void test() throws SQLException {
	  Batch73();
	  Batch74();
    // Linq has no sample 75
		Batch76();
		Batch77();
	  Batch78();
	  Batch79();
		Batch80();
	  Batch81();
	  Batch82();
		Batch83();
		Batch84();
	  Batch85();
	  Batch86();
		Batch87();
		Batch88();
	  Batch89();
	  Batch90();
		Batch91();
	  Batch92();
	  Batch93();
	}

}
