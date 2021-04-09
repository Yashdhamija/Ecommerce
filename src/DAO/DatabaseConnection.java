package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection { // DB class

	private Connection connection;
	private static DatabaseConnection instance;
	private Statement statement;
	private  final String JDBC = "jdbc:mysql://" + "data.czjngffm4iwg.us-east-1.rds.amazonaws.com" + ":" + "3306" + "/" + "bookstore" + "?user=" + "admin" + "&password=" + "Messenger1";

	
	
	
	private DatabaseConnection() throws SQLException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String jdbcUrl = DBConnect.JDBC;
			System.out.println("Getting remote connection with connection string from environment variables.");
			this.connection = DriverManager.getConnection(jdbcUrl);
			System.out.println("Remote connection successful.");

		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		} catch (SQLException e) {
			System.out.println(e.toString());
		}

	}
	
	public  void setStatement(Statement statement) {
		this.statement = statement;
	}
	
	public Connection getConnection() throws SQLException {
		 
		return this.connection;
	}

	public static DatabaseConnection getInstance() throws SQLException {

		if (instance == null) {

			instance = new DatabaseConnection();

		} else if (instance.getConnection().isClosed()) {

			instance = new DatabaseConnection();
		}

		return instance;

	}
	
	
	
	
	









	








	









	












}
