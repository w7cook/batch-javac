/*******************************************************************************
 * The accompanying source code is made available to you under the terms of 
 * the UT Research License (this "UTRL"). By installing or using the code, 
 * you are consenting to be bound by the UTRL. See LICENSE.html for a 
 * full copy of the license.
 * 
 * Copyright © 2009, The University of Texas at Austin. All rights reserved.
 * 
 * UNIVERSITY EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES CONCERNING THIS 
 * SOFTWARE AND DOCUMENTATION, INCLUDING ANY WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR ANY PARTICULAR PURPOSE, NON-INFRINGEMENT AND WARRANTIES 
 * OF PERFORMANCE, AND ANY WARRANTY THAT MIGHT OTHERWISE ARISE FROM COURSE 
 * OF DEALING OR USAGE OF TRADE. NO WARRANTY IS EITHER EXPRESS OR IMPLIED 
 * WITH RESPECT TO THE USE OF THE SOFTWARE OR DOCUMENTATION. Under no circumstances 
 * shall University be liable for incidental, special, indirect, direct 
 * or consequential damages or loss of profits, interruption of business, 
 * or related expenses which may arise from use of Software or Documentation, 
 * including but not limited to those resulting from defects in Software 
 * and/or Documentation, or loss or inaccuracy of data of any kind.
 * 
 * Created by: William R. Cook and Eli Tilevich
 * with: Jose Falcon, Marc Fisher II, Ali Ibrahim, Yang Jiao, Ben Wiedermann
 * University of Texas at Austin and Virginia Tech
 ******************************************************************************/
package sql.northwind.schema;

import batch.Fun;
import batch.sql.Column;
import batch.sql.Entity;
import batch.sql.Id;
import batch.sql.Inverse;
import batch.sql.Many;

@Entity(name="Products")
public class Product {

	@Id
	public int ProductID;

	public String ProductName;

	public String QuantityPerUnit;

	public double UnitPrice;

	public long UnitsInStock;

	public int UnitsOnOrder;

	public int ReorderLevel;

	public boolean Discontinued;

	@Column(name="CategoryID")
	public Category Category;

	@Column(name="SupplierID")
	public Supplier Supplier;

	@Inverse("Product")
	public Many<Order_Details> OrderDetails;
	
	public void delete() {}
	
	public static Fun<Product, Boolean> isOutOfStock = new Fun<Product, Boolean>() {
		public Boolean apply(Product p) {
			return p.UnitsInStock == 0;
		}
	};

	public static Fun<Product, String> byCategoryName = new Fun<Product, String>() {
		public String apply(Product p) {
			return p.Category.CategoryName;
		}
	};

	public static Fun<Product, Category> byCategory = new Fun<Product, Category>() {
		public Category apply(Product p) {
			return p.Category;
		}
	};

	public static Fun<Product, String> byName = new Fun<Product, String>() {
		public String apply(Product p) {
			return p.ProductName;
		}
	};

	public static Fun<Product, Long> byUnitsInStock = new Fun<Product, Long>() {
		public Long apply(Product p) {
			return p.UnitsInStock;
		}
	};

	public static Fun<Product, Double> byUnitPrice = new Fun<Product, Double>() {
		public Double apply(Product p) {
			return p.UnitPrice;
		}
	};

}
