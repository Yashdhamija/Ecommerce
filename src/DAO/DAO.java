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

	public int insertUserDB(String fname, String lname, String email, String password) throws SQLException { // Insert
																												// DB
																												// for
																												// new
																												// users

		count = getMaxAddressId();
		getRemoteConnection();

		String query = "INSERT INTO Users VALUES(?,?,?,?,?)";

		PreparedStatement ps = con.prepareStatement(query);

		ps.setString(1, fname);
		ps.setString(2, lname);
		ps.setString(3, email);
		ps.setString(4, password);
		ps.setInt(5, count);

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
			String query = "SELECT fname FROM Users WHERE email='" + email + "'";
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
			String query = "SELECT fname FROM Partners WHERE email='" + email + "'";
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
	
	
	public int insertAdminIntoDB(String email,String fname,String lname,String password) throws SQLException {
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
		return result;
	}

	public int insertPartnerDB(String email, String password, String fname, String lname) throws SQLException {
		getRemoteConnection();
		c = getMaxAddressId();

		String query = "INSERT INTO Partners VALUES(?,?,?,?,?)";

		PreparedStatement ps = con.prepareStatement(query);

		ps.setString(1, email);
		ps.setString(2, password);
		ps.setInt(3, c);
		ps.setString(4, fname);
		ps.setString(5, lname);
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;

	}

	public int insertReview(String fname, String lname, String bid, String review, String title) throws SQLException {

		getRemoteConnection();

		String query = "INSERT INTO Review VALUES(?,?,?,?,?,?)";

		PreparedStatement ps = con.prepareStatement(query);

		ps.setString(1, fname);
		ps.setString(2, lname);
		ps.setString(3, bid);
		ps.setString(4, review);
		ps.setString(5, null);
		ps.setString(6, title);
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
		int addressId = retrieveAddressId(email);
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
		ps.setInt(5, addressId);
		ps.setString(6, dateString);
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;
	}

	public int insertPurchaseOrderItem(int orderId, String bid, int price, int quantity) throws SQLException {

		//int orderId = this.retrieveOrderIdFromPO(email);
		System.out.println("This is the orderId from POItem"+orderId);
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

	public int insertAddress(String street, String province, String country, String zip, String phone, String city)
			throws SQLException {
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
		int result = ps.executeUpdate();
		ps.close();
		con.close();
		return result;
	}

	public boolean IsVisitorExistInDB(String email,String password) { // For Signing up, if previous email exists then it checks
		String dbEmail = null;
		String dbPassword = null;
		boolean visitorExist = false;
		getRemoteConnection();
		try {

			this.stmt = this.con.createStatement();
			String query = "SELECT email,password FROM Users WHERE email='" + email + "' AND password='" + password + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				dbEmail = rs.getString("email");
				dbPassword = rs.getString("password");
				System.out.println("This value inside the DB"+ dbEmail);
				System.out.println("This value inside the DB"+ dbPassword);
				// System.out.println(e);
			}
			if (dbEmail != null  && dbPassword != null) {
				
				visitorExist = true;
			}

		

			rs.close();
			ps.close();
			con.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		return visitorExist;
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
			list.add(new ReviewBean(fname, lname, bookid, review, title));

		}

		rs.close();
		ps.close();
		con.close();
		return list;

	}

	public int retrieveOrderIdFromPO(String email) throws SQLException {

		int addressId = this.retrieveAddressId(email);
		int orderId = 0;

		getRemoteConnection();
		List<ReviewBean> list = new ArrayList<ReviewBean>();

		String query = "SELECT * FROM PO WHERE address='" + addressId + "'";

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

	public int getMaxAddressId() throws SQLException {
		int maximum = 0;
		getRemoteConnection();
		String query = "SELECT Max(id) as max FROM Address";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			maximum = rs.getInt("max");
		}
		return maximum;
	}

	public boolean IsPartnerExistInDB(String email, String password) {

		String dbEmail =null;
		String dbPassword = null;
		boolean partnerExist = false;
		getRemoteConnection();
		try {
			this.stmt = this.con.createStatement();
			String query = "SELECT email,password FROM Partners WHERE password='" + password + "' AND email='" + email +"'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				dbPassword = rs.getString("password");
				dbEmail =  rs.getString("email");
				
			}
			if (dbEmail != null && dbPassword != null) {
				partnerExist = true;
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

		return partnerExist;

	}

	public String retrievePartnerEmail(String email) { // For Signing up, if previous email exists then it checks
		String s = "partner email doesn't exist";
		String u = null;
		getRemoteConnection();
		try {

			this.stmt = this.con.createStatement();
			String query = "SELECT email FROM Partners WHERE email='" + email + "'";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				u = rs.getString("email");
				// System.out.println(e);
			}
			if (u != null && u.equals(email)) {
				s = "partner email exists";
			}

			else {

				s = "partner email does not exists";

			}

			rs.close();
			ps.close();
			con.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}
	
	
	

    // Don't forgot to change this as someone can put the bid that isn't right by means of a subset of actual bid
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
		String btitle = "";
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
	
	public BookBean getProductJSON(String productId) throws SQLException {
        List<BookBean> l = new ArrayList<BookBean>();
        l = retreivebookrecord(productId);
        return l.get(0);

    }
	
	public List<OrderBean> getOrdersByPartNumber(String bid) throws SQLException {
		
        String orderId;
        String lname;
        String fname;
        String status;
        String address;
        String date;
        int quantity;
        int unitPrice;
        
        List<OrderBean> orderList = new ArrayList<>();
        getRemoteConnection();
        String query = String.format("SELECT * FROM PO,POItem WHERE PO.orderid=POItem.id AND POItem.bid='%s'", bid);
        PreparedStatement ps = con.prepareStatement(query);
//        ps.setString(1,bid);
        ResultSet rs = ps.executeQuery(query);


        while (rs.next()) {
            orderId = rs.getString("PO.orderid");
            lname = rs.getString("lname");
            fname = rs.getString("fname");
            status = rs.getString("status");
            address = rs.getString("address");
            date = rs.getString("date");
            quantity = rs.getInt("quantity");
            unitPrice = rs.getInt("price");
            OrderBean order = new OrderBean(orderId, quantity, unitPrice, lname, fname, status, address, date);
            orderList.add(order);

        }

        rs.close();
        ps.close();
        con.close();

        return orderList;

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

	public List<BookBean> retriveBookFromCategory(String category) throws SQLException {
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
		int aid2 = 0;
		// String query = "SELECT Partners.aid, Users.addressid FROM Users,Partners
		// WHERE Users.email='" + email + "' OR Partners.email='"+ email + "'";
		String query = "SELECT * FROM (SELECT Partners.aid FROM Partners WHERE Partners.email='" + email
				+ "'UNION ALL SELECT Users.aid " + "FROM Users WHERE Users.email='" + email + "') AS t";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			aid = rs.getInt("t.aid");

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

	public UserBean RetrievePartnerInfo(String email) throws SQLException {

		getRemoteConnection();
		String fname;
		String lname;
		UserBean user = null;
		String query = "SELECT * FROM Partners WHERE email='" + email + "'";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			fname = rs.getString("fname");
			lname = rs.getString("lname");
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

	public LinkedHashMap<String, LinkedHashMap<String, Integer>> getBooksSoldEachMonth() throws SQLException {
		getRemoteConnection();
		LinkedHashMap<String, LinkedHashMap<String, Integer>> result = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();

		String query = "select orderid,substring(`date`,1,7) as dates,POItem.bid,POItem.price,sum(quantity) as quantities, title from (POItem inner join PO on POItem.id=PO.orderid) join Book on POItem.bid = Book.bid group by POItem.bid order by dates, quantities desc;";
		PreparedStatement ps = con.prepareStatement(query);
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
		getRemoteConnection();
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> current = new ArrayList<String>();

		String query = "select PO.lname as lname, PO.fname as fname, sum(POItem.price*POItem.quantity) as total, Address.zip as zip from (PO join POItem on PO.orderid=POItem.id) join Address on Address.id=PO.address group by PO.fname, PO.lname";
		PreparedStatement ps = con.prepareStatement(query);
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
		// password parameter should be encrypted by using BookStoreModel.encryptPassword()
		getRemoteConnection();
		String p = null;
		try {
			this.stmt = this.con.createStatement();
			String query = "SELECT password FROM AdminBookStore WHERE password='" + password  + "'";
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
	
	
	

}
