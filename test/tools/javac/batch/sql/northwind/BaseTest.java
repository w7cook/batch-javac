package sql.northwind;

import java.sql.SQLException;

import sql.northwind.schema.Northwind;
import batch.sql.JDBC;
import batch.sql.SQLBatch;

public abstract class BaseTest {

	protected SQLBatch<Northwind> connection;
	
	public abstract void test() throws SQLException;

	public void run() throws SQLException {
		//		x.test(new SQLBatch<Northwind>(Northwind.class));
		// now again with JDBC
		String cstr = "jdbc:mysql://localhost/Northwind?user=root&password=";
		connection = new JDBC<Northwind>(Northwind.class, cstr);
		test();
	}
	
	protected void print(String format, Object... s) {
		int len = format.length();
		for (int i = 0; i < len; i++) {
			char c = format.charAt(i);
			if (c == '{') {
				int item = format.charAt(++i) - '0';
				System.out.print(s[item]);
				i++;
			} else
				System.out.print(c);
		}
		System.out.println("");
	}

}
