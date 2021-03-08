package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.BookBean;

public class DAO {

	private Connection con;
	private Statement stmt;

	public DAO() {

		getRemoteConnection();

	}

	public Connection getRemoteConnection() {
		// if (System.getProperty("RDS_HOSTNAME") != null) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String dbName = System.getProperty("RDS_DB_NAME");
			String userName = System.getProperty("RDS_USERNAME");
			String password = System.getProperty("RDS_PASSWORD");
			String hostname = System.getProperty("RDS_HOSTNAME");
			String port = System.getProperty("RDS_PORT");
			String jdbcUrl = "jdbc:mysql://" + "data.czjngffm4iwg.us-east-1.rds.amazonaws.com" + ":" + "3306" + "/"
					+ "bookstore" + "?user=" + "admin" + "&password=" + "Messenger1";
			System.out.println("Getting remote connection with connection string from environment variables.");
			this.con = DriverManager.getConnection(jdbcUrl);
			System.out.println("Remote connection successful.");
			return con;
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		// }
		return null;

	}

	public void insertUserDB(String fname, String lname, String email, String password) { // Insert DB for new users
																							// during
																							// signing up

		try {

			this.stmt = this.con.createStatement();
			System.out.println(fname);
			System.out.println(lname);
			String query = "INSERT INTO Users " + "VALUES ('" + fname + "', '" + lname + "', '" + email + "', '"
					+ password + "')";
			stmt.executeUpdate(query);
			// String query = "insert into customer values (null, '"+fname+"', '"+lname+"',
			// '"+tel+"', '"+email+"', '"+password+"')";

		} catch (SQLException se) {
			se.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void insertPartnerDB(int uid, String password) { // Insert DB for new users
															// during
		// signing up
			getRemoteConnection();
		try {

			this.stmt = this.con.createStatement();

			String query = "INSERT INTO Partners " + "VALUES ('" + uid + "', '" + password + "')";
			stmt.executeUpdate(query);
			
			
			con.close();

		} catch (SQLException se) {
			se.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public String retrieveEmail(String email) { // For Signing up, if previous email exists then it checks
		String s = null;
		String e = null;
		try {

			this.stmt = this.con.createStatement();
			String query = "SELECT email FROM Users WHERE email='" + email + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				e = rs.getString("email");
				// System.out.println(e);
			}
			if (e != null && e.equals(email)) {
				s = "email exists";
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	public String retrievePassword(String password) {
		getRemoteConnection();                              //added this
		String s = null;
		String p = null;
		try {
			this.stmt = this.con.createStatement();
			String query = "SELECT password FROM Users WHERE password='" + password + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				p = rs.getString("password");
				// System.out.println(e);
			}
			if (p != null && p.equals(password)) {
				s = "password exists";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return s;

	}

	public String retrievePartnerPassword(String password) {

		String s = null;
		String p = null;
		try {
			this.stmt = this.con.createStatement();
			String query = "SELECT password FROM Partners WHERE password='" + password + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				p = rs.getString("password");
				// System.out.println(e);
			}
			if (p != null && p.equals(password)) {
				s = "partner password exists";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return s;

	}

	public String retrieveUID(String uid) { // For Signing up, if previous email exists then it checks
		String s = null;
		String u = null ;
		try {

			this.stmt = this.con.createStatement();
			String query = "SELECT uid FROM Partners WHERE uid='" + uid + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				u = rs.getString("uid");
				// System.out.println(e);
			}
			if (u != null && u.equals(uid)) {
				s = "uid exists";
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	public List <BookBean> retreivebookrecord(String bid) throws SQLException {
		getRemoteConnection();
		List<BookBean> l = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book WHERE bid LIKE '%" + bid + "%'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = Integer.parseInt(rs.getString("price"));
			String category = rs.getString("category");
			String url = rs.getString("imageurl");
			l.add(new BookBean(bookid, btitle, bprice, category, url));
			
			
		}
		
		rs.close();
		ps.close();
		con.close();
		System.out.println(l.get(0).getBid());
		return l;
	}
	
	public List <BookBean> retrievebookinfo(String bid) throws SQLException {
		
		getRemoteConnection();
		List<BookBean> list = new ArrayList<BookBean>();             

		String query = "SELECT * FROM Book WHERE bid='" + bid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = Integer.parseInt(rs.getString("price"));
			String category = rs.getString("category");
			String url = rs.getString("imageurl");
			list.add(new BookBean(bookid, btitle, bprice, category, url));
			
			
		}
		
		rs.close();
		ps.close();
		con.close();
		return list;
		
	}
	
	
	public List <BookBean> retreivebook(String title) throws SQLException {
		getRemoteConnection();
		List<BookBean> l = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book WHERE title LIKE '%" + title + "%'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = Integer.parseInt(rs.getString("price"));
			String category = rs.getString("category");
			String url = rs.getString("imageurl");
			l.add(new BookBean(bookid, btitle, bprice, category, url));
			
			
		}
		
		rs.close();
		ps.close();
		con.close();
		return l;
	}
	
	
	public String numberOfSearchResults(String title) throws SQLException {
		getRemoteConnection();
		List<BookBean> l = new ArrayList<BookBean>();
		String s;
		String query = "SELECT * FROM Book WHERE title LIKE '%" + title + "%'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = Integer.parseInt(rs.getString("price"));
			String category = rs.getString("category");
			String url = rs.getString("imageurl");
			l.add(new BookBean(bookid, btitle, bprice, category, url));
			
			
		}
		s = l.size() + " search results found";
		rs.close();
		ps.close();
		con.close();
		return s;
	}
	
	
	

	
}
