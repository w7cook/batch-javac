package sql.northwind;

import java.sql.SQLException;

import sql.northwind.schema.Category;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Product;
import batch.sql.Group;

// other categories of queries
// * dynamic queries
// * updates (insert, delete, update)
// * find by id
// * grouping with summarizing and items
// * parameterized queries
// * modular queries
// *   aliases in the data model
// * where clause on a many-valued field

public class MoreTests extends BaseTest {
	public static void main(String[] args) throws SQLException {
		new MoreTests().run();
	}

	public void extraInsert() {
		int x = 0;
/*
		int Price = 0;
		String Name = "test";
		for (Northwind db : connection) {
		 for (Product product : db.Products)
		  if ((Price == 0 
		         || product.UnitPrice > Price)
		     && (Name.length() == 0 
		         || product.ProductName.contains(Name)))
		    print(product.ProductName);
		}
		*/
    for (Northwind db : connection) {
        Product p = db.Products.create();
        p.ProductName = "Output the new name";
        p.UnitPrice = 23.23;
   //     p.Category = db.Categories.id(3);
        x = p.ProductID;
    }
    
    for (Northwind db : connection) {
      for (Category c : db.Categories)
        if (c.CategoryName == "Test") {
          Product p = db.Products.create();
          p.ProductName = "Output the new name";
          p.UnitPrice = 23.23;
          p.Category = c;
          x = p.ProductID;
        }
    }
		System.out.println("answer is: " + x);
	}

	@SuppressWarnings("null")
	public void Update1() {
		print("***List products:");
		String nameCheck = null;
		for (Northwind db : connection) {
			for (Product product : db.Products)
				if (product.Category == db.Categories.id(3))
					print("product {0}", product.ProductName);
		}
		print("***Update products:");
		for (Northwind db : connection) {
			for (Product product : db.Products)
				if ((nameCheck == null || product.ProductName == nameCheck)
						&& product.UnitsInStock > 0) {
					product.UnitsInStock = product.UnitsInStock + 1;
					product.Supplier = db.Suppliers.id(3);
				}
			print("Sum = {0}", db.Products.sum(Product.byUnitsInStock));

			// delete!
			for (Category c : db.Categories)
				if (c.CategoryName == "Test Category")
					c.delete();
		}
		print("***Insert category:");
		for (Northwind db : connection) {
			// insert!
			Category c = db.Categories.create();
			c.CategoryName = "Test Category";
			c.Description = "yes, is this going to work??";
		}
	}

	public void Test3() {
		print("***** Test3");
		String name = "Test Product";
		for (Northwind db : connection) {
			Product p = db.Products.id(11);
			print("Product '{0}' price {1}", p.ProductName, p.UnitPrice);
		}
		print("***Delete products:");
		for (Northwind db : connection) {
			for (Product p : db.Products)
				if (p.ProductName == name)
					p.delete();
		}
		/* GRRR.. NOT WORKING.. TODO! 
		double price = 23.23;
		print("***Insert product into category:");
		for (Northwind db : connection) {
			Product p = db.Categories.id(3).Products.create();
			p.ProductName = name;
			p.UnitPrice = price;
		}
*/
	}

	public void Linq42b() {
		print("***** Test42b");
		/*
		for (Northwind db : connection) {
			for (Group<String, Product> g : db.Products
					.groupBy(Product.byCategoryName)) {
				print("Category '{0}' has {1} average {2}:", g.Key, g.Items
						.count(), g.Items.average(Product.byUnitPrice));
				for (Product p : g.Items)
					print("  Product '{0}':", p.ProductName);
			}
		}*/
	}

	// The following example uses the Average operator to find those Products
	// whose unit price is higher than the average unit price of the category it
	// belongs to. The example then displays the results in groups.
	// var priceQuery =
	//   from prod in db.Products
	//   group prod by prod.CategoryID into grouping
	//   select new {
	//     grouping.Key,
	//     ExpensiveProducts =
	//        from prod2 in grouping
	//        where prod2.UnitPrice > grouping.Average(prod3 => prod3.UnitPrice)
	//        select prod2
	//      };
	//
	// foreach (var grp in priceQuery) {
	//   Console.WriteLine(grp.Key);
	//   foreach (var listing in grp.ExpensiveProducts)
	//     Console.WriteLine(listing.ProductName);
	// }

	public void Linq999A() {
		print("***** Test999A");
		for (Northwind db : connection) {
			for (Group<Category, Product> group : db.Products.groupBy(Product.byCategory)) {
				print("Group {0}", group.Key.CategoryName);
				for (Product product : group.Items)
					if (product.UnitPrice > group.Items.average(Product.byUnitPrice))
						print("  {0}", product.ProductName);
			}
		}
	}

	// Grouping by a category ID should not be needed?

	public void Linq999B() {
		print("***** Test999B");
		for (Northwind db : connection) {
			for (Product product : db.Products.orderBy(Product.byCategoryName, true))
				if (product.UnitPrice > product.Category.Products
						.average(Product.byUnitPrice))
					print("{0} {1}", product.Category.CategoryName, product.ProductName);
		}
	}

	public void Linq999C() {
		print("***** Test999C");
		for (Northwind db : connection) {
			for (Category category : db.Categories) {
				print("Group {0}", category.CategoryName);
				for (Product product : category.Products)
					/* BUG: "translated table": when the condition gets moved down to the
					 * subquery it tries to bind to the outer "c" not the
					 * C introduced by the sub-query below:
					 */
					if (product.UnitPrice > category.Products
							.average(Product.byUnitPrice))
						print("  {0}", product.ProductName);
			}
		}
	}


	public void test() throws SQLException {
		extraInsert();
 		Test3();
		Update1();
		Linq42b();
		Linq999A();
		Linq999B();
		Linq999C();
	}

}
