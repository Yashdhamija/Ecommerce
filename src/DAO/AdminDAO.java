package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import bean.UserBean;

public class AdminDAO {
	
	
	private static DatabaseConnection admin;
	
	public AdminDAO() throws SQLException {
		
		this.admin =   DatabaseConnection.getInstance();
		
		
	}
	
	
	
	public LinkedHashMap<String, Integer> getTopTenAllTime() throws SQLException {
		
		LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();
		// "SELECT bid, sum(quantity) AS num FROM bookstore.PO where status != "DENIED"
		// GROUP BY bid ORDER BY num DESC"
		String query = "SELECT title, POItem.bid as bid, sum(quantity) AS num FROM POItem join Book on POItem.bid=Book.bid GROUP BY bid ORDER BY num DESC LIMIT 10";

		PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("title");
			Integer count = rs.getInt("num");
			list.put(bookid, count);
		}
		return list;
	}

	public LinkedHashMap<String, LinkedHashMap<String, Integer>> getBooksSoldEachMonth() throws SQLException {
	
		LinkedHashMap<String, LinkedHashMap<String, Integer>> result = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();

		String query = "select PO.orderid,substring(`date`,1,7) as dates,POItem.bid,POItem.price,sum(quantity) as quantities, title from (POItem inner join PO on POItem.orderid=PO.orderid) join Book on POItem.bid = Book.bid group by POItem.bid order by dates, quantities desc;";
		PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		rs.next();
		String date = rs.getString("dates");
		list.put(rs.getString("title"), rs.getInt("quantities"));
		while (rs.next()) {
			if (!rs.getString("dates").equals(date)) {
				if (date.endsWith("-")) {
					result.put(date.substring(0, date.length() - 1), list);
				} else {
					result.put(date, list);
				}

				date = rs.getString("dates");
				list = new LinkedHashMap<String, Integer>();
				list.put(rs.getString("title"), rs.getInt("quantities"));
			} else {
				list.put(rs.getString("title"), rs.getInt("quantities"));
			}
		}

		if (!list.isEmpty()) {
			if (date.endsWith("-")) {
				result.put(date.substring(0, date.length() - 1), list);
			} else {
				result.put(date, list);
			}
		}
		rs.close();
		return result;
	}

	public List<List<String>> getUserStatistics() throws SQLException {
		
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> current = new ArrayList<String>();

		String query = "select PO.lname as lname, PO.fname as fname, sum(POItem.price*POItem.quantity) as total, Address.zip as zip from (PO join POItem on PO.orderid=POItem.orderid) join Address on Address.cid=PO.cid group by PO.fname, PO.lname";
		PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			current.add("****      ***");
			current.add(rs.getString("zip"));
			current.add(rs.getString("total"));
			result.add(current);
			current = new ArrayList<String>();
		}

		return result;
	}

	public String getAdminPwd(String password) {
		// password parameter should be encrypted by using
		// BookStoreModel.encryptPassword()
	
		String p = null;
		try {
			
			String query = "SELECT password FROM AdminBookStore WHERE password=?";
			PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
			ps.setString(4, password);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				p = rs.getString("password");
			}
			rs.close();
			this.admin.getConnection().close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	// Either password or username since they are unique
	public UserBean getAdminInfo(String password) {
		
		
		
		String adminPassword ="";
		String email = "";
		String firstName = "";
		String lastName = "";
	
		UserBean admin = null;
		try {
			
			String query = "SELECT password FROM AdminBookStore WHERE password=?";
			PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
			ps.setString(4, password);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				adminPassword = rs.getString("password");
				email = rs.getString("email");
				firstName = rs.getString("fname");
				lastName = rs.getString("lname");
				
				admin =  new UserBean(firstName,lastName,email,adminPassword);
			}
			rs.close();
			this.admin.getConnection().close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return admin;
		
		
		
	}

	public String getAdminEmail(String email) {
	
		String p = null;
		try {
			
			String query = "SELECT email FROM AdminBookStore WHERE email=?";
			PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				p = rs.getString("email");
			}
			rs.close();
			this.admin.getConnection().close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	
	public void insertAdminIntoDB(String email, String fname, String lname, String password) throws SQLException {
		
		String query = "INSERT INTO AdminBookStore VALUES(?,?,?,?)";
		PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
		ps.setString(1, email);
		ps.setString(2, lname);
		ps.setString(3, fname);
		ps.setString(4, password);
		int result = ps.executeUpdate();
		ps.close();
		this.admin.getConnection().close();
		// return result;
	}
	
	
	
	public boolean IsAdminValidated(String user,String password) {
		String dbEmail = null;
		String dbPassword = null;
		boolean isAdmin = false;
	
		try {

			
			String query = "SELECT email,password FROM AdminBookStore WHERE email=? AND password=?"
					+ password + "'";
			PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				dbEmail = rs.getString("email");
				dbPassword = rs.getString("password");

			}

			if (dbEmail != null && dbPassword != null) {

				isAdmin = true;

			}

			rs.close();
			this.admin.getConnection().close();

		} catch (SQLException se) {
			se.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return isAdmin;
	}




}
