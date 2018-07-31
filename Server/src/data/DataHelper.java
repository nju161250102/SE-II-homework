package data;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataHelper {

	private static Connection connection;
	private static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=ERPDB";
	private static String userName = "sa";
	private static String userPwd = "Orz151099124";

	private DataHelper() {
		
	}
	
	public static Connection getInstance() {
		if (connection == null) {
			try {
				Class.forName(driverName);
				connection = DriverManager.getConnection(dbURL,userName,userPwd);
				System.out.println("Succeed!"); 
			} catch(Exception e) {
				e.printStackTrace();
				System.out.print("fail!");
			}
		}
		return connection;
	}
}
