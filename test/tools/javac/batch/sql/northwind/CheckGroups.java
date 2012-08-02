package sql.northwind;

import java.sql.Date;
import java.sql.SQLException;

import sql.northwind.schema.Category;
import sql.northwind.schema.Customer;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Order;
import sql.northwind.schema.Product;
import batch.sql.Group;

public class CheckGroups extends BaseTest {
	private void Linq18() {
		print("***** Linq18");
		Date cutoffDate = Date.valueOf("1997-01-01");
		for (Northwind db : connection) {
			for (Customer c : db.Customers)
				if (c.Region == "WA")
					for (Order o : c.Orders)
						if (o.OrderDate.after(cutoffDate))
							print("Customer={0} Order={1}",
									c.CustomerID,
									o.OrderID);
		}
	}

	private void LinqX() {
		print("****For/Cond/For*****");
		for (Northwind db : connection) {
			for (Category c : db.Categories) {
				for (Product p : c.Products)
					if (c.CategoryName == p.ProductName)
						print("  Order {0}", p.ProductName);
			}
		}
	}

	private void LinqA() {
		print("****Group/For*****");
		/*
		for (Northwind db : connection) {
			for (Group<Category, Product> g : db.Products
					.groupBy(Product.byCategory)) {
				print("Category={0}", g.Key.CategoryName);
				for (Product p : g.Items)
					print("  Products: ProductName={0}", p.ProductName);
			}
		}*/
	}

	private void LinqB() {
		print("****Group/Aggregate*****");
		/*
		for (Northwind db : connection) {
			for (Group<Category, Product> g : db.Products
					.groupBy(Product.byCategory)) {
				print("Category={0} #{1}", g.Key.CategoryName, g.Items.count());
			}
		}*/
	}

	private void LinqC() {
		print("****For/Group*****");
		/*
		for (Northwind db : connection) {
			for (Category c : db.Categories) {
				print("Category={0}", c.CategoryName);
				for (Group<Boolean, Product> p : c.Products
						.groupBy(Product.isOutOfStock))
					print(" {0} #{1}", p.Key, p.Items.count());
			}
		}*/
	}

	private void LinqD() {
		print("****Group/Group*****");
		/*
		for (Northwind db : connection) {
			for (Group<Category, Product> g : db.Products
					.groupBy(Product.byCategory)) {
				print("Category={0}", g.Key.CategoryName);
				for (Group<Boolean, Product> p : g.Items
						.groupBy(Product.isOutOfStock))
					print(" {0} #{1}", p.Key, p.Items.count());
			}
		}*/
	}

	public void test() throws SQLException {
		Linq18();
		LinqX();
		LinqA();
		LinqB();
		LinqC();
		LinqD();
	}

	public static void main(String[] args) throws SQLException {
		new CheckGroups().run();
	}

}
