package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.OrderBean;
import bean.ReviewBean;

public class PODAO {
	
	
	private static DatabaseConnection purchaseOrder;
	private UserDAO user;

	public PODAO() throws SQLException {

		this.purchaseOrder = DatabaseConnection.getInstance();
		this.user = new UserDAO();

	}

	public int insertPurchaseOrder(int orderId, String fname, String lname, String status, String email)
			throws SQLException {
		int customerId = this.user.retrieveCustomerId(email);
		this.purchaseOrder = DatabaseConnection.getInstance();
		String query = "INSERT INTO PO VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = this.purchaseOrder.getConnection().prepareStatement(query);
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
		this.purchaseOrder.getConnection().close();
		return result;
	}
	
	
	public int retrieveOrderIdFromPO(String email) throws SQLException {

		int customerId = this.user.retrieveCustomerId(email);
		int orderId = 0;
		List<ReviewBean> list = new ArrayList<ReviewBean>();

		this.purchaseOrder = DatabaseConnection.getInstance();
		String query = "SELECT * FROM PO WHERE address = ?";
		PreparedStatement ps = this.purchaseOrder.getConnection().prepareStatement(query);
		ps.setInt(1, customerId);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			orderId = rs.getInt("orderid");

		}

		rs.close();
		ps.close();
		this.purchaseOrder.getConnection().close();
		return orderId;
	}
	
	public ArrayList<String> getAllUserOrderDates(String email) throws SQLException {
		
		String dates = null;
		ArrayList<String> arr = new ArrayList<String>();
		this.purchaseOrder = DatabaseConnection.getInstance();
		String query = "Select distinct date from PO inner join POItem item ON PO.orderid = item.orderid INNER JOIN Address a ON a.cid = PO.cid INNER JOIN Users u ON u.customerid = a.cid AND email = ? order by date desc";
		PreparedStatement ps = this.purchaseOrder.getConnection().prepareStatement(query);
		ps.setString(1,email);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			dates = rs.getString("date");
			arr.add(dates);
		}
		
		rs.close();
		ps.close();
		this.purchaseOrder.getConnection().close();
		return arr;
	}
	
	public List<OrderBean>  getOrderByDate(String date, String email) {
		
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
			this.purchaseOrder = DatabaseConnection.getInstance();
			String query = "Select b.imageurl, PO.orderid, date, b.bid, status,item.price, quantity from PO inner join POItem item ON PO.orderid = item.orderid INNER JOIN Address a ON a.cid = PO.cid INNER JOIN Users u ON u.customerid = a.cid AND email = ? INNER JOIN Book b ON b.bid = item.bid AND date = ?";
			PreparedStatement ps = this.purchaseOrder.getConnection().prepareStatement(query);
			ps.setString(1,email);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();
			
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
			this.purchaseOrder.getConnection().close();
			

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderList;
	}
	
	
	public ArrayList<String> getDates() throws SQLException {
		
		
		String date;
		ArrayList<String> arr = new ArrayList<String>();
		this.purchaseOrder = DatabaseConnection.getInstance();
		String query = "select distinct concat(year(date), '-',  month(date)) as dates from PO ORDER BY dates desc";
		PreparedStatement ps = this.purchaseOrder.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			date = rs.getString("dates");	
			arr.add(date);		
		}
		
		rs.close();
		ps.close();
		this.purchaseOrder.getConnection().close();
		return arr;
		
	}
	



}
