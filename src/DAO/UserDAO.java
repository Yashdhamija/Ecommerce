package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bean.UserBean;

public class UserDAO {
	
	private static DatabaseConnection user;

	
	public UserDAO() throws SQLException {
		
		this.user = DatabaseConnection.getInstance();
		
	}
	
	
	public int insertuser(String fname, String lname, String email, String password) throws SQLException {

		String query = "INSERT INTO Users VALUES(?,?,?,?,?,?)";

		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
		ps.setString(1, null);
		ps.setString(2, fname);
		ps.setString(3, lname);
		ps.setString(4, email);
		ps.setInt(5, 0); // Usertype 0 is customer
		ps.setString(6, password);

		int result = ps.executeUpdate();
		ps.close();
		this.user.getConnection().close();
		return result;

	}
	
	
	public String getCustomerName(String email) {
		String name = "";

		try {

			// this.user.setStatement(user.getConnection().createStatement());
			String query = "SELECT fname FROM Users WHERE email=? AND usertype=0";
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ps.setString(4, email);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				name = rs.getString("fname");

			}

			rs.close();
			ps.close();
			this.user.getConnection().close();

		} catch (SQLException se) {
			se.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(name);
		return name;

	}
	
	
//	public boolean isCustomerExistInDB(String email, String password) { // For Signing up, if previous email exists then
//		// it checks
//		String dbEmail = null;
//		String dbPassword = null;
//		boolean visitorExist = false;
//		try {
//
//			//this.user.setStatement(user.getConnection().createStatement());
//			String query = "SELECT email,password FROM Users WHERE email=? AND password=?"
//				+ " AND usertype=0";
//			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
//			ps.setString(4,email);
//			ps.setString(6, password);
//			ResultSet rs = ps.executeQuery(query);
//
//			while (rs.next()) {
//				dbEmail = rs.getString("email");
//				dbPassword = rs.getString("password");
//				System.out.println("This value inside the DB" + dbEmail);
//				System.out.println("This value inside the DB" + dbPassword);
//
//			}
//			if (dbEmail != null && dbPassword != null) {
//
//				visitorExist = true;
//			}
//
//			rs.close();
//			ps.close();
//			this.user.getConnection().close();
//
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//		}
//
//		catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//		return visitorExist;
//	}
	
	
	public boolean doesUserExistInDB(String email, String password) { // For Signing up, if previous email exists then
		// it checks
		String dbEmail = null;
		String dbPassword = null;
		boolean visitorExist = false;
		try {

			//this.user.setStatement(user.getConnection().createStatement());
			String query = "(SELECT email,password FROM Users WHERE email=? AND password=? AND usertype=0) UNION"
					+ "(SELECT email,password FROM Users WHERE email=? AND password=? AND usertype=1)";
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ps.setString(4,email);
			ps.setString(6, password);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				dbEmail = rs.getString("email");
				dbPassword = rs.getString("password");
				System.out.println("This value inside the DB" + dbEmail);
				System.out.println("This value inside the DB" + dbPassword);

			}
			if (dbEmail != null && dbPassword != null) {

				visitorExist = true;
			}

			rs.close();
			ps.close();
			this.user.getConnection().close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		return visitorExist;
	}
	
	
	
//	public UserBean retrieveAllUserInfo(String email) throws SQLException {
//		
//		String fname;
//		String lname;
//		String userEmail;
//		String password;
//		int customerId;
//		UserBean user = null;
//		String query = "SELECT * FROM Users WHERE email=?";
//
//		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
//		ps.setString(4, email);
//		ResultSet rs = ps.executeQuery();
//
//		while (rs.next()) {
//			fname = rs.getString("fname");
//			lname = rs.getString("lname");
//			userEmail = rs.getString("email");
//			password = rs.getString("password");
//			customerId
//			user = new UserBean(fname, lname, userEmail,password);
//		}
//
//		rs.close();
//		ps.close();
//		this.user.getConnection().close();
//		return user;
//
//	}
//	
	
	public UserBean retrieveCustomerInfo(String email,String password) throws SQLException {
		
		String fname;
		String lname;
		String customerEmail;
		String customerPassword;
		int customerId;
		int userType;
		UserBean user = null;
		String query =  "(SELECT email,password FROM Users WHERE email=? AND password=? AND usertype=0) UNION"
				+ "(SELECT email,password FROM Users WHERE email=? AND password=? AND usertype=1)";
		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
		ps.setString(4, email);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			fname = rs.getString("fname");
			lname = rs.getString("lname");
			customerEmail = rs.getString("email");
			password = rs.getString("password");
			customerId =  rs.getInt("customerid");
			userType = rs.getInt("usertype");
			user = new UserBean(fname, lname, customerEmail,password,customerId,userType);
		}

		rs.close();
		ps.close();
		this.user.getConnection().close();
		return user;

	}
	
	
//public UserBean retrievePartnerInfo(String email) throws SQLException {
//		
//		String fname;
//		String lname;
//		String partnerEmail;
//		String password;
//		int customerId;
//		UserBean user = null;
//		String query = "SELECT * FROM Users WHERE email=? AND usertype=1";
//		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
//		ps.setString(4, email);
//		ResultSet rs = ps.executeQuery();
//
//		while (rs.next()) {
//			fname = rs.getString("fname");
//			lname = rs.getString("lname");
//			partnerEmail = rs.getString("email");
//			password = rs.getString("password");
//			customerId = rs.getInt("customerid");
//			user = new UserBean(fname, lname, partnerEmail,password,customerId);
//		}
//
//		rs.close();
//		ps.close();
//		this.user.getConnection().close();
//		return user;
//
//	}



public int retrieveCustomerId(String email) throws SQLException {

	int customerId = 0;

	String query = "SELECT customerid FROM Users WHERE email=?";

	PreparedStatement ps = this.user.getConnection().prepareStatement(query);
	ps.setString(4,email);
	ResultSet rs = ps.executeQuery();

	while (rs.next()) {

		customerId = rs.getInt("customerid");

	}
	rs.close();
	ps.close();
	this.user.getConnection().close();
	return customerId;
}


//public boolean IsPartnerExistInDB(String email, String password) {
//
//	String dbEmail = null;
//	String dbPassword = null;
//	boolean partnerExist = false;
//
//	try {
//		//this.stmt = this.con.createStatement();
//		String query = "SELECT email,password FROM Users WHERE password=? AND email=? AND usertype=1";
//		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
//		ps.setString(4, email);
//		ps.setString(6, password);
//		ResultSet rs = ps.executeQuery(query);
//
//		while (rs.next()) {
//			dbPassword = rs.getString("password");
//			dbEmail = rs.getString("email");
//
//		}
//		if (dbEmail != null && dbPassword != null) {
//			partnerExist = true;
//		}
//		rs.close();
//		ps.close();
//		this.user.getConnection().close();
//
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
//
//	catch (Exception e) {
//		e.printStackTrace();
//	}
//
//	return partnerExist;
//
//}



public String retrievePassword(String password) {
	
	String str = null;
	String p = null;
	try {
		//this.stmt = this.con.createStatement();
		String query = "SELECT password FROM Users WHERE password=?";
		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
		ps.setString(6, password);
		ResultSet rs = ps.executeQuery(query);

		while (rs.next()) {
			p = rs.getString("password");
			// System.out.println(e);
		}
		if (p != null) {
			str = "password exists";
		} else {
			str = "password doesnt match, try again";
		}

		rs.close();
		ps.close();
		this.user.getConnection().close();

	} catch (SQLException e) {
		e.printStackTrace();
	}

	catch (Exception e) {
		e.printStackTrace();
	}

	return str;

}


public String getPartnerName(String email) {
	String name = "";
	
	try {

		//this.stmt = this.con.createStatement();
		String query = "SELECT fname FROM Users WHERE email=? AND usertype=1";
		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
		ps.setString(4, email);
		ResultSet rs = ps.executeQuery(query);
		while (rs.next()) {
			name = rs.getString("fname");

		}

		rs.close();
		this.user.getConnection().close();

	} catch (SQLException se) {
		se.printStackTrace();
	}

	catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println(name);
	return name;
}


public int insertPartnerDB(String fname, String lname, String email, String password) throws SQLException {
	

	String query = "INSERT INTO Users VALUES(?,?,?,?,?,?)";
	System.out.println("The length is" + fname.length());
	System.out.println("The length is" + lname.length());
	PreparedStatement ps = this.user.getConnection().prepareStatement(query);
	ps.setString(1, null);
	ps.setString(2, fname);
	ps.setString(3, lname);
	ps.setString(4, email);
	ps.setInt(5, 1); // 1 is for partners and 0 is customers
	ps.setString(6, password);
	int result = ps.executeUpdate();
	ps.close();
	this.user.getConnection().close();
	return result;

}



	

}
