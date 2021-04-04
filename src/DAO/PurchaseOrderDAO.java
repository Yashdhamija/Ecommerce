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

public class PurchaseOrderDAO {

	private static DatabaseConnection purchaseOrder;
	private UserDAO user;

	public PurchaseOrderDAO() throws SQLException {

		this.purchaseOrder = DatabaseConnection.getInstance();
		this.user = new UserDAO();

	}

	public int insertPurchaseOrder(int orderId, String fname, String lname, String status, String email)
			throws SQLException {

		String query = "INSERT INTO PO VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = this.purchaseOrder.getConnection().prepareStatement(query);
		int customerId = this.user.retrieveCustomerId(email);
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

	public List<OrderBean> getOrdersByPartNumber(String bid) throws SQLException {

		String orderId;
		String lname;
		String fname;
		String status;
		String date;
		int quantity;
		int unitPrice;
		int customerId;

		List<OrderBean> orderList = new ArrayList<>();

		String query = String.format("SELECT * FROM PO,POItem WHERE PO.orderid=POItem.orderid AND POItem.bid=?");
		PreparedStatement ps = this.purchaseOrder.getConnection().prepareStatement(query);
		ps.setString(2, bid);
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
			OrderBean order = new OrderBean(orderId, quantity, unitPrice, lname, fname, status, customerId, date);
			orderList.add(order);

		}

		rs.close();
		ps.close();
		this.purchaseOrder.getConnection().close();

		return orderList;

	}

	public int retrieveOrderIdFromPO(String email) throws SQLException {

		int customerId = this.user.retrieveCustomerId(email);
		int orderId = 0;

		List<ReviewBean> list = new ArrayList<ReviewBean>();

		String query = "SELECT * FROM PO WHERE address='" + customerId + "'";

		PreparedStatement ps = this.purchaseOrder.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			orderId = rs.getInt("orderid");

		}

		rs.close();
		ps.close();
		this.purchaseOrder.getConnection().close();
		return orderId;

	}

}
