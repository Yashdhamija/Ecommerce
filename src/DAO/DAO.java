package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.AddressBean;
import bean.BookBean;
import bean.ReviewBean;
import bean.UserBean;

public class DAO { // DB class

	private Connection con;
	private Statement stmt;
	int count;
	int c;
	public DAO() {

		getRemoteConnection();

	}

	public Connection getRemoteConnection() {
		// if (System.getProperty("RDS_HOSTNAME") != null) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String jdbcUrl = DBConnect.JDBC;
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

	public void insertUserDB(String fname, String lname, String email, String password) throws SQLException { // Insert DB for new users
																							// during
		count = getMaxAddressId();																				// signing up
		getRemoteConnection();
					
		String query = "INSERT INTO Users VALUES(?,?,?,?,?)";

		PreparedStatement ps = con.prepareStatement(query);
	
		ps.setString(1, fname);
		ps.setString(2, lname);
		ps.setString(3, email);
		ps.setString(4, password);
		ps.setInt(5, count);
		
		int rs = ps.executeUpdate();
	
	
}

	public String getFullName(String email) {
		String name="";
		getRemoteConnection();
		try {
			
			this.stmt = this.con.createStatement();
			String query = "SELECT fname,lname FROM Users WHERE email='" + email + "'";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				name = rs.getString("fname");
				
			}

			rs.close();
			con.close();

		} catch (SQLException se) {
			se.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(name);
		return name;

	}

	public void insertPartnerDB(int uid, String password, String fname, String lname) throws SQLException { // Insert DB for new users
															// during
		// signing up
		getRemoteConnection();
		c = getMaxAddressId();	
		
		String query = "INSERT INTO Partners VALUES(?,?,?,?,?)";

		PreparedStatement ps = con.prepareStatement(query);
	
		ps.setInt(1, uid);
		ps.setString(2, password);
		ps.setInt(3, c);
		ps.setString(4, fname);
		ps.setString(5, lname);
		int rs = ps.executeUpdate();
		

	}
	
	
	public void insertReview(String fname, String lname, String bid, String review) throws SQLException {
		
		getRemoteConnection();
		
		String query = "INSERT INTO Review VALUES(?,?,?,?,?)";

		PreparedStatement ps = con.prepareStatement(query);
		
		ps.setString(1, fname);
		ps.setString(2, lname);
		ps.setString(3, bid);
		ps.setString(4, review);
		ps.setString(5, null);
		int rs = ps.executeUpdate();
			
	}
	
	public void insertPurchaseOrder(int orderId, String fname, String lname, String status, String email)
			throws SQLException {
		getRemoteConnection();
		String query = "INSERT INTO PO VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);	
		int addressId = retrieveAddressId(email);
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year  = localDate.getYear();
		int month = localDate.getMonthValue();
		int day   = localDate.getDayOfMonth();
		String dateString = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
		ps.setInt(1, orderId);
		ps.setString(2, fname);
		ps.setString(3, lname);
		ps.setString(4, status);
		ps.setInt(5, addressId);
		ps.setString(6, dateString);
	    int rs = ps.executeUpdate();
	}
	
	public void insertAddress(String street, String province, String country, String zip, String phone, String city) throws SQLException {
		getRemoteConnection();
		String query = "INSERT INTO Address VALUES(?,?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);	
		
		ps.setString(1, null);
		ps.setString(2, street);
		ps.setString(3, province);
		ps.setString(4, country);
		ps.setString(5, zip);
		ps.setString(6, phone);
		ps.setString(7, city);
		int rs = ps.executeUpdate();
	}

	public String retrieveEmail(String email) { // For Signing up, if previous email exists then it checks
		String s = null;
		String e = null;
		getRemoteConnection();
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


			rs.close();
			con.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		return s;
	}
	
	
	public List<ReviewBean> retriveReviews(String bid) throws SQLException {
		
		getRemoteConnection();
		List<ReviewBean> list = new ArrayList<ReviewBean>();
		
		
		String query = "SELECT * FROM Review WHERE bid='" + bid + "' ORDER BY reviewid DESC limit 2";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String bookid = rs.getString("bid");
			String review = rs.getString("review");
			list.add(new ReviewBean(fname, lname, bookid, review));

		}

		rs.close();
		ps.close();
		con.close();
		return list;
		
	}
	
	

	public String retrievePassword(String password) {
		getRemoteConnection(); // added this
		String str = null;
		String p = null;
		try {
			this.stmt = this.con.createStatement();
			String query = "SELECT password FROM Users WHERE password='" + password + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				p = rs.getString("password");
				// System.out.println(e);
			}
			if (p != null) {
				str = "password exists";
			}
			else {
				str = "password doesnt match, try again";
			}

			rs.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return str;

	}
	
	// Retrieves the price of individual book using bid as a parameter
	
	public int retrievePriceofSingleBook(String bid) throws SQLException {
		getRemoteConnection();
		int price = 0;
		
		String query = "SELECT price FROM Book WHERE bid='" + bid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			price = rs.getInt("price");
		}

		rs.close();
		ps.close();
		con.close();
		return price;
	}
	
	public int getMaxAddressId() throws SQLException {
		int maximum = 0;
		getRemoteConnection();
		String query = "SELECT Max(id) as max FROM Address";
		PreparedStatement ps = con.prepareStatement(query);	
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			maximum = rs.getInt("max");
		}
		return maximum;
	}

	public String retrievePartnerPassword(String password) {

		String s = null;
		String p = null;
		getRemoteConnection();
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
			else {
				s = "incorrect password provided";
			}

			rs.close();
			con.close();

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
		String u = null;
		getRemoteConnection();
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

			rs.close();
			con.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}
	
	

	public List<BookBean> retreivebookrecord(String bid) throws SQLException {
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
	
	public String retrieveSingleBookTitle(String bid) throws SQLException {
		String btitle = null;
		getRemoteConnection();

		String query = "SELECT title FROM Book WHERE bid='" + bid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			btitle = rs.getString("title");
		}

		rs.close();
		ps.close();
		con.close();
		return btitle;
	}
	
	public List<BookBean> retrievebookinfo(String bid) throws SQLException {

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

	public List<BookBean> retreivebook(String title) throws SQLException {
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
	
	public List<BookBean> retriveBookFromCategory(String category) throws SQLException{
		getRemoteConnection();
		List<BookBean> list = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book WHERE category='" + category + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = Integer.parseInt(rs.getString("price"));
			String cat = rs.getString("category");
			String url = rs.getString("imageurl");
			list.add(new BookBean(bookid, btitle, bprice, cat, url));

		}

		rs.close();
		ps.close();
		con.close();
		return list;
		
		
		
	}
	
	
	public int retrieveAddressId(String email) throws SQLException {
		getRemoteConnection();
		int aid = 0;
		String query = "SELECT addressid FROM Users WHERE email='" + email + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			 aid = rs.getInt("addressid");

		}

		rs.close();
		ps.close();
		con.close();
		return aid;
	}
	
	
	
	public AddressBean retrieveAddressById(String email) throws SQLException {
		int addressid = retrieveAddressId(email);
		int aid;
		String street;
		String province;
		String city;
		String zip;
		String country;
		String phone;
		
		getRemoteConnection();
		AddressBean address = null;
		
		String query = "SELECT * FROM Address WHERE id='" + addressid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			 aid = rs.getInt("id");
			 street = rs.getString("street");
			 province = rs.getString("province");
			 city = rs.getString("city");
			 zip = rs.getString("zip");
			 country = rs.getString("country");
			 phone = rs.getString("phone");
			 address = new AddressBean(country, province, city, street, zip, phone);
		}

		rs.close();
		ps.close();
		con.close();
		return address;
		
	}
	
	
	public UserBean retrieveAllUserInfo(String email) throws SQLException {
		getRemoteConnection();
		String fname;
		String lname;
		String e;
		UserBean user = null;
		String query = "SELECT * FROM Users WHERE email='" + email + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			 fname = rs.getString("fname");
			 lname = rs.getString("lname");
			 e = rs.getString("email");
			 user = new UserBean(fname, lname, email);
		}

		rs.close();
		ps.close();
		con.close();
		return user;
		
		
		
		
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
