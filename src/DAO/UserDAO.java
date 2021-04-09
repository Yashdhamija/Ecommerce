package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import bean.UserBean;

public class UserDAO {

	private DatabaseConnection user;

	public UserDAO() throws SQLException {

		this.user = DatabaseConnection.getInstance();

	}

	public int insertUserDB(String fname, String lname, String email, String password) throws SQLException {
		this.user = DatabaseConnection.getInstance();
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
			this.user = DatabaseConnection.getInstance();
			String query = "SELECT fname FROM Users WHERE email = ? AND usertype=0";
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
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

	public String getPartnerName(String email) {
		String name = "";

		try {
			this.user = DatabaseConnection.getInstance();
			String query = "SELECT fname FROM Users WHERE email = ? AND usertype=1";
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
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
	
	// returns the partner key associated with the email
	public String getpartnerKey(String email) {
		
		String key = null;
		
		try {
			this.user = DatabaseConnection.getInstance();
			String query = "SELECT apikey FROM PartnerKeys WHERE email='" + email + "'";
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);	
			
			while (rs.next()) {
				key = rs.getString("apikey") ; 
			}
			
			rs.close();
			ps.close();
			this.user.getConnection().close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return key;
	}
	
	public boolean isValidPartnerKey(String key) {
		
		try {
			this.user = DatabaseConnection.getInstance();
			String query = "SELECT * FROM PartnerKeys WHERE apikey='" + key + "'";
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);	
			
			if (rs.next()) {
				return true; 
			}
			
			rs.close();
			ps.close();
			this.user.getConnection().close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}

	public int insertPartnerDB(String fname, String lname, String email, String password) throws SQLException {
		this.user = DatabaseConnection.getInstance();
		String query = "INSERT INTO Users VALUES(?,?,?,?,?,?)";
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
	
	public int insertPartnerKey(String email) throws SQLException {
		
		// email already present in PartnersKeys table with associated key
		// just reuse previously generated key
		if (this.emailHasKey(email)) {
			System.out.println("[dao.insertPartnerKey] A Key already exists with email: " + email);
			return 0;
		} else {
			this.user = DatabaseConnection.getInstance();
			String uuid = UUID.randomUUID().toString();
			String query = "INSERT INTO PartnerKeys VALUES(?,?)";
			System.out.println("Inserting partner key-> partner email is: " + email + 
					", uniquely generated key is: " + uuid);
			
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, uuid);
			
			int result = ps.executeUpdate();
			
			ps.close();
			this.user.getConnection().close();
						
			return result;	
		}
	}

	public boolean isEmailTaken(String email) {

		int userType = -1;

		try {
			this.user = DatabaseConnection.getInstance();
			String query = "SELECT fname, usertype FROM Users WHERE email = ?";
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				userType = rs.getInt("usertype");
				System.out.println(
						"This user inside the DB: Name -> " + rs.getString("fname") + " userType -> " + userType);
			}

			rs.close();
			ps.close();
			this.user.getConnection().close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return userType != -1;
	}
	
	public boolean emailHasKey(String email) {
		
		try {
			this.user = DatabaseConnection.getInstance();
			String query = "SELECT email FROM PartnerKeys WHERE email='" + email + "'";
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);	
			
			if (rs.next()) {
				return true;
			}
			
			rs.close();
			ps.close();
			this.user.getConnection().close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}

	// checks if users with given credential sexists in dB.
	// Generic functionality, can be used to check for all kinds of users:
	// customers, partners & administrators
	// @returns - UserBean (fname, userType ) if users exists else returns null
	public UserBean isUserExist(String email, String password) {

		UserBean user = null;
		String fname, lname;
		int userType, customerId;
		try {
			this.user = DatabaseConnection.getInstance();
			String query = "SELECT fname, lname, usertype, customerid FROM Users WHERE email = ? AND password = ?";
		

			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				fname = rs.getString("fname");
				lname = rs.getString("lname");
				userType = rs.getInt("usertype");
				customerId = rs.getInt("customerid");
				user = new UserBean(fname, lname, email, userType, customerId);
				System.out.println("This user inside the DB: Name -> " + fname + " userType -> " + userType);
			}

			rs.close();
			ps.close();
			this.user.getConnection().close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return user;
	}

	public int retrieveCustomerId(String email) throws SQLException {

		int customerId = 0;
		this.user = DatabaseConnection.getInstance();
		String query = "SELECT customerid FROM Users WHERE email = ?";

		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
		ps.setString(1, email);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			customerId = rs.getInt("customerid");

		}
		rs.close();
		ps.close();
		this.user.getConnection().close();
		return customerId;
	}

	public UserBean retrieveAllUserInfo(String email) throws SQLException {

		String fname;
		String lname;
		int userType, customerId;
		UserBean user = null;
		String query = "SELECT * FROM Users WHERE email = ?";
		this.user = DatabaseConnection.getInstance();
		PreparedStatement ps = this.user.getConnection().prepareStatement(query);
		ps.setString(1, email);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			
			fname = rs.getString("fname");
			lname = rs.getString("lname");
			userType = rs.getInt("userType");
			customerId = rs.getInt("customerid");
			user = new UserBean(fname, lname, email, userType, customerId);
		}

		rs.close();
		ps.close();
		this.user.getConnection().close();
		return user;

	}
	
	public String retrievePassword(String password) {
		
		String str = null;
		String p = null;
		try {
			
			String query = "SELECT password FROM Users WHERE password = ?";
			this.user = DatabaseConnection.getInstance();
			PreparedStatement ps = this.user.getConnection().prepareStatement(query);
			ps.setString(1, password);
			ResultSet rs = ps.executeQuery();

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


}
