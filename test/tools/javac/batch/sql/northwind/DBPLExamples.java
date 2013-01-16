package sql.northwind;

import java.sql.SQLException;

import sql.northwind.schema.Category;
import sql.northwind.schema.Customer;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Product;
import batch.Fun;
import batch.sql.Group;
import batch.IncludeInBatch;

public class DBPLExamples extends BaseTest {
	public static void main(String[] args) throws SQLException {
		new DBPLExamples().run();
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

	/*
	 for (Product p : db.Products) {
	    if (p.InStock > 0) {
        OUT("A", p.ProductName);
        OUT("B", p.Category.CategoryName);
        OUT("C", p.UnitPrice);
        for (Order o : p.orders) {
          OUT("D", o.date)
        }
      }
    }

   SELECT p.ProductName as A, p.Category.CategoryName as B, p.UnitPrice as C
   WHERE  p.InStock > 0
   FROM PRODUCTS p INNER JOIN CATEGORY c ON p.category = c.id
   



	 */
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
			for (Product p : db.Products.orderBy(Product.byName))
				print("Name={0} UnitPrice={1}", p.ProductName, p.UnitPrice);
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
				print("CustomerID={0}\t OrderCount={1}", 
					  c.CustomerID, c.Orders.count());
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
		Fun<Product, Category> byCategory = new Fun<Product, Category>() {
			@IncludeInBatch public Category apply(Product p) {
				return p.Category;
			}
		};
		Fun<Product, Long> byUnitsInStock = new Fun<Product, Long>() {
			@IncludeInBatch public Long apply(Product p) {
				return p.UnitsInStock;
			}
		};
		
		for (Northwind db : connection)
			for (Group<Category, Product> g : db.Products.groupBy(byCategory))
				print("Category={0}\t TotalUnitsInStock={1}",
						g.Key.CategoryName,
						g.Items.sum(byUnitsInStock));
	}
	
	/* BAW: Doesn't compile
	public void interProceduralBatch2() {
		print("***** Batch2");
		for (Northwind db : connection) {
			print("Sold out products:");
			for (Product product : db.Products)
				if (Batch2Test(product))
					print("{0} is sold out!", product.ProductName);
		}
	}
	
	public boolean Batch2Test(Product product) {
		return product.UnitsInStock == 0;
	}
	*/
	
	public void countProducts() {
		for (Northwind db : connection)
			print("Number of products: {0}", db.Products.count());
	}
	
	public int insertProduct() {
		print("***Insert product:");
		int id = -1;
		for (Northwind db : connection) {
			Product p = db.Products.create();
			p.ProductName = "New Widget";
			p.UnitPrice = 23.23;
			//p.Category = db.Categories.id(3);			// TODO: Causes a bug, if uncommented
			id = p.ProductID;
		}
		return id;
	}
	
	public void updateProductByName(String name, double newPrice) {
		print("***Updating product by name:");
		for (Northwind db : connection)
			for (Product p : db.Products)
				if (p.ProductName == name)
					p.UnitPrice = newPrice;					
	}
	
	// BAW: Fails with error: "Combined actions" at batch.sql.syntax.SQLQuery.doAction(SQLQuery.java:357)
	public void updateProductByID(int id, double newPrice) {
		print("***Updating product by id:");
		for (Northwind db : connection)
			db.Products.id(id).UnitPrice = newPrice;
	}
	
	public void deleteProductByName(String name) {
		print("***Delete product:");
		for (Northwind db : connection)
			for (Product p : db.Products)
				if (p.ProductName == name)
					p.delete();
	}
	
	// BAW: Generates incorrect SQL: "DELETE T1" (needs from and where clause)
	public void deleteProductByID(int id) {
		print("***Delete product:");
		for (Northwind db : connection) {
			db.Products.id(id).delete();
		}
	}
	
	public void showPriceByID(int id) {
		for (Northwind db : connection) {
			Product p = db.Products.id(id);
			print("Price: {0}", p.UnitPrice);
		}
	}
	
	public void insertProductIntoSet() {
		print("***Insert product into category:");
		/*
		for (Northwind db : connection) {
			Product p = db.Categories.id(3).Products.create();
			p.ProductName = "New Widget";
			p.UnitPrice = 20.00;
		}
		*/
	}
	
	/* BAW: Doesn't compile
	public void bulkInsert(String[] categories) {
		int[] newIDs = new int[categories.length];
		for (Northwind db : connection) {
			for (int idx=0; idx<categories.length; idx++) {
				Category c = db.setupCategory;
				c.CategoryName = categories[idx];
				newIDs[idx] = Integer.parseInt(db.insertCategory(c));
			}
		}
	}
	*/
	
	public void bulkDiscount() {
		print("***Bulk discount:");
		for (Northwind db : connection) {
			for (Product p : db.Products)
				if (p.Category.CategoryName == "Produce")
					p.UnitPrice *= .9;
		}
	}
	
	public void test() throws SQLException {
		Batch2();
		//interProceduralBatch2();
		//Batch7();
		Batch11();
		//Batch30();
		Batch76();
		//Batch80();
		
		countProducts();
		int newID = insertProduct();
		System.out.println("New id is: " + newID);
		countProducts();
		updateProductByName("New Widget", 30.00);
		//updateProductByID(newID, 30.00);
		countProducts();
		deleteProductByName("New Widget");
		//deleteProductByID(newID);
		countProducts();
		
		insertProductIntoSet();
		countProducts();
		deleteProductByName("New Gadget");
		countProducts();
		
		bulkDiscount();
		
		// interprocedural (query)?
	}

}
