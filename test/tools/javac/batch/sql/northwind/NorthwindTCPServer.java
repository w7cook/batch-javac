package sql.northwind;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

import batch.EvalService;
import batch.json.JSONTransport;
import batch.syntax.Evaluate;
import batch.tcp.TCPServer;
import batch.util.BatchTransport;
import batch.sql.JDBC;
import batch.sql.SQLBatch;
import batch.sql.syntax.SQLTranslation;

import sql.northwind.schema.Northwind;

public class NorthwindTCPServer {
  public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);

    try {
      runServer(port, new JSONTransport());
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String conPath =
    "jdbc:mysql://localhost/Northwind?user=root&password=";

  private static void runServer(int port, BatchTransport transport)
      throws IOException, SQLException {
    SQLBatch<Northwind> connection =
      new JDBC<Northwind>(Northwind.class, conPath);
    TCPServer<SQLTranslation, Northwind> server =
      new TCPServer<SQLTranslation, Northwind>(
        connection, new ServerSocket(port), transport
      );
    server.debug = true;
    server.start();
  }
}
