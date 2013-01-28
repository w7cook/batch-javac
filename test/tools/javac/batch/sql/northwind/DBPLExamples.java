package sql.northwind;

import java.sql.SQLException;

import sql.northwind.schema.Category;
import sql.northwind.schema.Customer;
import sql.northwind.schema.Northwind;
import sql.northwind.schema.Product;
import batch.Fun;
import batch.IncludeInBatch;
import batch.sql.Group;

public class DBPLExamples extends BaseTest {
  public static void main(String[] args) throws SQLException {
    new DBPLExamples().run();
  }

  @IncludeInBatch
  public boolean Batch2Test(Product product) {
    return product.UnitsInStock == 0;
  }

  public void interProceduralBatch2() {
    print("***** Batch2");
    for (Northwind db : connection) {
      print("Sold out products:");
      for (Product product : db.Products)
        if (Batch2Test(product))
          print("{0} is sold out!", product.ProductName);
    }
  }

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
    for (Northwind db : connection) {
      Product p = db.Categories.id(3).Products.create();
      p.ProductName = "New Widget";
      p.UnitPrice = 20.00;
    }
  }

  public void bulkInsert(String[] categories) {
    print("**Bulkinsert***");
    /* TODO: Not working!
    int[] newIDs = new int[categories.length];
    for (Northwind db : connection) {
      for (int idx = 0; idx < categories.length; idx++) {
        Category c = db.Categories.create();
        c.CategoryName = categories[idx];
        newIDs[idx] = c.CategoryID;
      }
    }
        print("New IDS: " + newIDs);
    */
  }

  public void bulkDiscount() {
    print("***Bulk discount:");
    for (Northwind db : connection) {
      for (Product p : db.Products)
        if (p.Category.CategoryName == "Produce")
          p.UnitPrice *= .9;
    }
  }

  public void test() throws SQLException {
    interProceduralBatch2();

    countProducts();
    int newID = insertProduct();
    System.out.println("New id is: " + newID);
    countProducts();
    updateProductByName("New Widget", 30.00);
    updateProductByID(newID, 30.00);
    countProducts();
    deleteProductByName("New Widget");
    deleteProductByID(newID);
    countProducts();
    insertProductIntoSet();
    countProducts();
    deleteProductByName("New Gadget");
    countProducts();

    bulkInsert(new String[] { "TEST1", "TEST2" });
    bulkDiscount();
  }

}
