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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import bean.AddressBean;
import bean.BookBean;
import bean.OrderBean;
import bean.ReviewBean;
import bean.UserBean;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

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

	public int insertUserDB(String fname, String lname, String email, String password) throws SQLException {

		getRemoteConnection();

		String query = "INSERT INTO Users VALUES(?,?,?,?,?,?)";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, null);
		ps.setString(2, fname);
		ps.setString(3, lname);
		ps.setString(4, email);
		ps.setInt(5, 0); // Usertype 0 is customer
		ps.setString(6, password);

		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;

	}

	public String getCustomerName(String email) {
		String name = "";
		getRemoteConnection();
		try {

			this.stmt = this.con.createStatement();
			String query = "SELECT fname FROM Users WHERE email='" + email + "' AND usertype=0";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				name = rs.getString("fname");

			}

			rs.close();
			ps.close();
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

	public String getPartnerName(String email) {
		String name = "";
		getRemoteConnection();
		try {

			this.stmt = this.con.createStatement();
			String query = "SELECT fname FROM Users WHERE email='" + email + "' AND usertype=1";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
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

	public boolean IsAdminValidated(String email, String password) {
		String dbEmail = null;
		String dbPassword = null;
		boolean isAdmin = false;
		getRemoteConnection();
		try {

			this.stmt = this.con.createStatement();
			String query = "SELECT email,password FROM AdminBookStore WHERE email='" + email + "' AND password='"
					+ password + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				dbEmail = rs.getString("email");
				dbPassword = rs.getString("password");

			}

			if (dbEmail != null && dbPassword != null) {

				isAdmin = true;

			}

			rs.close();
			con.close();

		} catch (SQLException se) {
			se.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return isAdmin;
	}

	public void insertAdminIntoDB(String email, String fname, String lname, String password) throws SQLException {
		getRemoteConnection();
		String query = "INSERT INTO AdminBookStore VALUES(?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, email);
		ps.setString(2, lname);
		ps.setString(3, fname);
		ps.setString(4, password);
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		// return result;
	}

	public int insertPartnerDB(String fname, String lname, String email, String password) throws SQLException {
		getRemoteConnection();

		String query = "INSERT INTO Users VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, null);
		ps.setString(2, fname);
		ps.setString(3, lname);
		ps.setString(4, email);
		ps.setInt(5, 1); // 1 is for partners and 0 is customers
		ps.setString(6, password);
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;

	}
	
	public int insertPartnerKey(String email) throws SQLException {
		getRemoteConnection();
		
		// email already present in PartnersKeys table with associated key
		// just reuse previously generated key
		if (this.emailHasKey(email)) {
			System.out.println("[dao.insertPartnerKey] A Key already exists with email: " + email);
			return 0;
		} else {
			String uuid = UUID.randomUUID().toString();
			String query = "INSERT INTO PartnerKeys VALUES(?,?)";
			System.out.println("Inserting partner key-> partner email is: " + email + 
					", uniquely generated key is: " + uuid);
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, uuid);
			
			int result = ps.executeUpdate();
			ps.close();
			con.close();
			
			return result;	
		}
	}

	public int insertReview(String fname, String lname, String bid, String review, String title, int rating) throws SQLException {

		getRemoteConnection();

		String query = "INSERT INTO Review VALUES(?,?,?,?,?,?,?)";

		PreparedStatement ps = con.prepareStatement(query);

		ps.setString(1, fname);
		ps.setString(2, lname);
		ps.setString(3, bid);
		ps.setString(4, review);
		ps.setString(5, null);
		ps.setString(6, title);
		ps.setInt(7, rating);
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;

	}

	public int insertPurchaseOrder(int orderId, String fname, String lname, String status, String email)
			throws SQLException {
		getRemoteConnection();
		String query = "INSERT INTO PO VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		int customerId = retrieveCustomerId(email);
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		String dateString = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
		ps.setInt(1, orderId);
		ps.setString(2, fname);
		ps.setString(3, lname);
		ps.setString(4, status);
		ps.setInt(5, customerId);
		ps.setString(6, dateString);
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;
	}

	public int insertPurchaseOrderItem(int orderId, String bid, int price, int quantity) throws SQLException {

		// int orderId = this.retrieveOrderIdFromPO(email);
		System.out.println("This is the orderId from POItem" + orderId);
		getRemoteConnection();
		String query = "INSERT INTO POItem VALUES(?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);

		ps.setInt(1, orderId);
		ps.setString(2, bid);
		ps.setInt(3, price);
		ps.setInt(4, quantity);
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;

	}

	public int insertAddress(String email, String street, String province, String country, String zip, String phone,
			String city) throws SQLException {
		getRemoteConnection();
		String query = "INSERT INTO Address VALUES(?,?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		int customerid = retrieveCustomerId(email);
		ps.setInt(1, customerid);
		ps.setString(2, street);
		ps.setString(3, province);
		ps.setString(4, country);
		ps.setString(5, zip);
		ps.setString(6, phone);
		ps.setString(7, city);
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;
	}
	
	public boolean isEmailTaken(String email) {
		getRemoteConnection();
		int userType = -1;
		
		try {
			String query = "SELECT fname, usertype FROM Users WHERE email='" + email + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);		
			
			while (rs.next()) {
				userType = rs.getInt("usertype");
				System.out.println("This user inside the DB: Name -> " + rs.getString("fname") + " userType -> " + userType);
			}
			
			rs.close();
			ps.close();
			con.close();	

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return userType != -1;
	}
	
	// checks if users with given credential sexists in dB.
	// Generic functionality, can be  used to check for all kinds of users: 
	// customers, partners & administrators
	// @returns - UserBean (fname, userType ) if users exists else returns null
	public UserBean isUserExist(String email, String password) {
		getRemoteConnection();
		UserBean user = null;
		String fname, lname;
		int userType, customerId;
		try {
			String query = "SELECT fname, lname, usertype, customerid FROM Users WHERE email='" + email + 
					"' AND password='" + password + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			
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
			con.close();	

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return user;
	}
	
	public boolean emailHasKey(String email) {
		getRemoteConnection();
		
		try {
			String query = "SELECT email FROM PartnerKeys WHERE email='" + email + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);	
			
			if (rs.next()) {
				return true;
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	// returns the partner key associated with the email
	public String getpartnerKey(String email) {
		getRemoteConnection();
		String key = null;
		
		try {
			String query = "SELECT apikey FROM PartnerKeys WHERE email='" + email + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);	
			
			while (rs.next()) {
				key = rs.getString("apikey") ; 
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return key;
	}
	
	public boolean isValidPartnerKey(String key) {
		getRemoteConnection();
		
		try {
			String query = "SELECT * FROM PartnerKeys WHERE apikey='" + key + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);	
			
			if (rs.next()) {
				return true; 
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}

	public List<ReviewBean> retriveReviews(String bid) throws SQLException {

		getRemoteConnection();
		List<ReviewBean> list = new ArrayList<ReviewBean>();

		String query = "SELECT * FROM Review WHERE bid='" + bid + "' ORDER BY reviewid DESC limit 3";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String bookid = rs.getString("bid");
			String review = rs.getString("review");
			String title = rs.getString("title");
			int rating = rs.getInt("rating");
			list.add(new ReviewBean(fname, lname, bookid, review, title, rating));

		}

		rs.close();
		ps.close();
		con.close();
		return list;

	}
	
	public List<ReviewBean> retriveAllReviews() throws SQLException {

		getRemoteConnection();
		List<ReviewBean> list = new ArrayList<ReviewBean>();

		String query = "SELECT * FROM Review";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String bookid = rs.getString("bid");
			String review = rs.getString("review");
			String title = rs.getString("title");
			int rating = rs.getInt("rating");
			list.add(new ReviewBean(fname, lname, bookid, review, title, rating));

		}

		rs.close();
		ps.close();
		con.close();
		return list;

	}
	

	public int retrieveOrderIdFromPO(String email) throws SQLException {

		int customerId = this.retrieveCustomerId(email);
		int orderId = 0;

		getRemoteConnection();
		List<ReviewBean> list = new ArrayList<ReviewBean>();

		String query = "SELECT * FROM PO WHERE address='" + customerId + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			orderId = rs.getInt("orderid");

		}

		rs.close();
		ps.close();
		con.close();
		return orderId;
	}

	public String retrievePassword(String password) {
		getRemoteConnection(); // added this
		String str = null;
		String p = null;
		try {
			this.stmt = this.con.createStatement();
			String query = "SELECT password FROM Users WHERE password='" + password + "'";
			PreparedStatement ps = con.prepareStatement(query);
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

	// Don't forgot to change this as someone can put the bid that isn't right by
	// means of a subset of actual bid
	public List<BookBean> retrieveAllBooks() throws SQLException { // This is for search functionality improve this
		getRemoteConnection();
		List<BookBean> l = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = rs.getInt("price");
			String category = rs.getString("category");
			String url = rs.getString("imageurl");
			l.add(new BookBean(bookid, btitle, bprice, category, url));
		}

		rs.close();
		ps.close();
		con.close();
		return l;
	}

	public String retrieveSingleBookTitle(String bid) throws SQLException { // change this to the below method grabbing all information return bookbean and just get title
		String btitle = "";
		getRemoteConnection();

		String query = "SELECT title FROM Book WHERE bid=?";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, bid);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			btitle = rs.getString("title");
		}

		rs.close();
		ps.close();
		con.close();
		return btitle;
	}
	
	
	public  BookBean retrieveBook(String bid) throws SQLException { // change this to the below method grabbing all information return bookbean and just get title
		BookBean book = null;
		getRemoteConnection();

		String query = "SELECT * FROM Book WHERE bid='" + bid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			book = new BookBean(rs.getString("bid"), 
					rs.getString("title"),
					rs.getInt("price"),
					rs.getString("category"),
					rs.getString("imageurl"));			
		}

		rs.close();
		ps.close();
		con.close();
		return book;
	}


	public BookBean getProductJSON(String productId) throws SQLException {
		return retrievebookinfo(productId);
	}

	public List<OrderBean> getOrdersByPartNumber(String bid) throws SQLException {

		String orderId;
		String lname;
		String fname;
		String status;
		int customerId;
		String date;
		int quantity;
		int unitPrice;

		List<OrderBean> orderList = new ArrayList<>();
		getRemoteConnection();
		String query = String.format("SELECT * FROM PO,POItem WHERE PO.orderid=POItem.orderid AND POItem.bid='%s'",
				bid);
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery(query);

		while (rs.next()) {
			orderId = rs.getString("PO.orderid");
			lname = rs.getString("lname");
			fname = rs.getString("fname");
			status = rs.getString("status");
			customerId = rs.getInt("cid");
			date = rs.getString("date");
			quantity = rs.getInt("quantity");
			unitPrice = rs.getInt("price");
			OrderBean order = new OrderBean(orderId, quantity, unitPrice, lname, fname, status, customerId, date, rs.getString("bid"), "");
			orderList.add(order);

		}

		rs.close();
		ps.close();
		con.close();

		return orderList;

	}

	public BookBean retrievebookinfo(String bid) throws SQLException {
		getRemoteConnection();
		BookBean bookinfo = null;
		String query = "SELECT * FROM Book WHERE bid='" + bid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			bookinfo = new BookBean(rs.getString("bid"), rs.getString("title"),
					rs.getInt("price"), rs.getString("category"), rs.getString("imageurl"));
		}

		rs.close();
		ps.close();
		con.close();
		return bookinfo;

	}

	public List<BookBean> retreivebook(String title) throws SQLException {
		getRemoteConnection();
		List<BookBean> l = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book WHERE title=?";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, title);
		
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

	public List<BookBean> retriveBookFromCategory(String category) throws SQLException {
		getRemoteConnection();
		List<BookBean> list = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book WHERE category=?";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, category);
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

	public int retrieveCustomerId(String email) throws SQLException {
		getRemoteConnection();
		int customerId = 0;

		String query = "SELECT customerid FROM Users WHERE email='" + email + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			customerId = rs.getInt("customerid");

		}
		rs.close();
		ps.close();
		con.close();
		return customerId;
	}

	public AddressBean retrieveAddressByEmail(String email) throws SQLException {
		int customerid = retrieveCustomerId(email);
		String street;
		String province;
		String city;
		String zip;
		String country;
		String phone;

		getRemoteConnection();
		AddressBean address = null;

		String query = "SELECT * FROM Address WHERE cid='" + customerid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
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
	
	public AddressBean retrieveAddressByCustomerId(int cid) throws SQLException {
		String street;
		String province;
		String city;
		String zip;
		String country;
		String phone;

		getRemoteConnection();
		AddressBean address = null;

		String query = "SELECT * FROM Address WHERE cid='" + cid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
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
		int userType, customerId;
		UserBean user = null;
		String query = "SELECT * FROM Users WHERE email='" + email + "'";

		PreparedStatement ps = con.prepareStatement(query);
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
		con.close();
		return user;

	}

	public String retrieveUrlOfSingleBook(String bid) throws SQLException {
		getRemoteConnection();
		String url = null;
		String query = "SELECT imageurl FROM Book WHERE bid='" + bid + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			url = rs.getString("imageurl");
		}

		rs.close();
		ps.close();
		con.close();
		return url;
	}

	public LinkedHashMap<String, Integer> getTopTenAllTime() throws SQLException {
		getRemoteConnection();
		LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();
		// "SELECT bid, sum(quantity) AS num FROM bookstore.PO where status != "DENIED"
		// GROUP BY bid ORDER BY num DESC"
		String query = "SELECT title, POItem.bid as bid, sum(quantity) AS num FROM POItem join Book on POItem.bid=Book.bid GROUP BY bid ORDER BY num DESC LIMIT 10";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("title");
			Integer count = rs.getInt("num");
			list.put(bookid, count);
		}
		return list;
	}

	
	
	public ArrayList<List<String>> getBooksSoldEachMonth(String date) throws SQLException {
		getRemoteConnection();
		String solddate ;
		String quantity;
		String title;
		ArrayList<List<String>> arr = new ArrayList<List<String>>();;
		List <String> l;
		
		String query = "select  concat(year(date), '-',  month(date)) as dates, sum(quantity) as quantity, title from PO p , POItem item, Book b where concat(year(date), '-',  month(date))='" + date + "'" +"AND p.orderid = item.orderid AND item.bid = b.bid GROUP BY dates, title ORDER BY quantity desc" ; 
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			solddate = rs.getString("dates");
			quantity = rs.getString("quantity");
			title = rs.getString("title");
			l = new ArrayList<>();
			l.add(solddate);
			l.add(quantity);
			l.add(title);
			arr.add(l);
					
		}
		
		return arr;
		
	}
	
	
	
	public ArrayList<String> getDates() throws SQLException {
		
		getRemoteConnection();
		String date;
		ArrayList<String> arr = new ArrayList<String>();
		
		String query = "select distinct concat(year(date), '-',  month(date)) as dates from PO ORDER BY dates desc";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			date = rs.getString("dates");	
			arr.add(date);		
		}
		
		rs.close();
		ps.close();
		con.close();
		return arr;
		
	}
	
	public ArrayList<String> getAllUserOrderDates(String email) throws SQLException {
		getRemoteConnection();
		String dates = null;
		ArrayList<String> arr = new ArrayList<String>();
		String query = "Select distinct date from PO inner join POItem item ON PO.orderid = item.orderid INNER JOIN Address a ON a.cid = PO.cid INNER JOIN Users u ON u.customerid = a.cid AND email='" + email + "'order by date desc";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			dates = rs.getString("date");
			arr.add(dates);
		}
		
		rs.close();
		ps.close();
		con.close();
		return arr;
	}

	public List<List<String>> getUserStatistics() throws SQLException {
		getRemoteConnection();
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> current = new ArrayList<String>();

		String query = "select PO.lname as lname, PO.fname as fname, sum(POItem.price*POItem.quantity) as total, Address.zip as zip from (PO join POItem on PO.orderid=POItem.orderid) join Address on Address.cid=PO.cid group by PO.fname, PO.lname order by total desc";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			//current.add("****      ***");
			current.add(rs.getString("fname")+rs.getString("lname"));
			current.add(rs.getString("zip"));
			current.add(rs.getString("total"));
			result.add(current);
			current = new ArrayList<String>();
		}
		
		rs.close();
		ps.close();
		con.close();
		return result;
	}

	public String getAdminPwd(String password) {
		// password parameter should be encrypted by using
		// BookStoreModel.encryptPassword()
		getRemoteConnection();
		String p = null;
		try {
			this.stmt = this.con.createStatement();
			String query = "SELECT password FROM AdminBookStore WHERE password='" + password + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				p = rs.getString("password");
			}
			rs.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return p;
	}

	public String getAdminEmail(String email) {
		getRemoteConnection();
		String p = null;
		try {
			this.stmt = this.con.createStatement();
			String query = "SELECT email FROM AdminBookStore WHERE email='" + email + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				p = rs.getString("email");
			}
			rs.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public void deleteReview(String bid, String review) {
		getRemoteConnection();
		try {
			this.stmt = this.con.createStatement();
			String query = "DELETE FROM Review WHERE bid='" + bid + "' AND review='" + review + "'";
			PreparedStatement ps = con.prepareStatement(query);
			int rs = ps.executeUpdate(query);
			
			
			
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public List<OrderBean>  getOrderByDate(String date, String email) {
		getRemoteConnection();
		String orderId;
		String status;
		int customerId = 0;
		String d;
		int quantity;
		int unitPrice;
		String bid;
		String url;
		List<OrderBean> orderList = new ArrayList<>();
		try {
			this.stmt = this.con.createStatement();
			String query = "Select b.imageurl, PO.orderid, date, b.bid, status,item.price, quantity from PO inner join POItem item ON PO.orderid = item.orderid INNER JOIN Address a ON a.cid = PO.cid INNER JOIN Users u ON u.customerid = a.cid AND email='" + email + "'" + "INNER JOIN Book b ON b.bid = item.bid AND date='" + date + "'";;
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			
			while(rs.next()) {
				orderId = rs.getString("orderid");
				quantity = rs.getInt("quantity");
				unitPrice = rs.getInt("price");
				status = rs.getString("status");
				d = rs.getString("date");
				bid = rs.getString("bid");
				url = rs.getString("imageurl");
				orderList.add(new OrderBean(orderId, quantity, unitPrice, "", "", status, customerId, date, bid, url));
				
			}
			
			rs.close();
			con.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderList;
	}
}                
