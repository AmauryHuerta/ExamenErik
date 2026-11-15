import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public final class TestTidbConnection {
  public static void main(String[] args) throws Exception {
    if (args.length != 3) {
      System.err.println("Usage: TestTidbConnection <host> <port> <username>");
      System.exit(2);
    }

    Console console = System.console();
    if (console == null) {
      throw new IllegalStateException("Run this script from an interactive PowerShell window.");
    }

    String host = args[0];
    int port = Integer.parseInt(args[1]);
    String username = args[2];
    char[] password = console.readPassword("DB_PASSWORD (input hidden): ");
    if (password == null || password.length == 0) {
      throw new IllegalArgumentException("A password is required.");
    }

    Properties properties = new Properties();
    properties.setProperty("user", username);
    properties.setProperty("password", new String(password));
    properties.setProperty("connectTimeout", "10000");
    properties.setProperty("socketTimeout", "10000");
    java.util.Arrays.fill(password, '\0');

    String url = "jdbc:mysql://" + host + ":" + port
        + "/identity?sslMode=VERIFY_IDENTITY&serverTimezone=UTC";
    try (Connection connection = DriverManager.getConnection(url, properties);
         Statement statement = connection.createStatement();
         ResultSet result = statement.executeQuery("SELECT VERSION(), DATABASE()")) {
      if (!result.next()) {
        throw new IllegalStateException("The database returned no verification row.");
      }
      System.out.println("Connection OK (TLS VERIFY_IDENTITY)");
      System.out.println("Database version: " + result.getString(1));
      System.out.println("Database: " + result.getString(2));
    } finally {
      properties.remove("password");
    }
  }
}
