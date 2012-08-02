package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import sql.northwind.CheckGroups;

public class LINQ101 extends BaseTest {
	public static void main(String[] args) throws SQLException {
		new LINQ101().run();
	}

	public void test() throws SQLException {
		new LINQRestrict().run();
		new LINQProject().run();
		new LINQPartition().run();
		new LINQOrdering().run();
		new LINQGroup().run();
		new LINQSet().run();
		new LINQQuantifier().run();
		new LINQAggregate().run();
		new LINQtestSimple().run();
		//new MoreTests().run();
		new CheckGroups().run();
	}

}
