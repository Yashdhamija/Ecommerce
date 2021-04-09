package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import bean.OrderBean;

public class POItemDAO {

	private static DatabaseConnection purchaseOrderItem;
	private UserDAO user;

	public POItemDAO() throws SQLException {

		this.purchaseOrderItem = DatabaseConnection.getInstance();
		this.user = new UserDAO();

	}

	public int insertPurchaseOrderItem(int orderId, String bid, int price, int quantity) throws SQLException {

		
		this.purchaseOrderItem = DatabaseConnection.getInstance();

		String query = "INSERT INTO POItem VALUES(?,?,?,?)";
		PreparedStatement ps = this.purchaseOrderItem.getConnection().prepareStatement(query);

		ps.setInt(1, orderId);
		ps.setString(2, bid);
		ps.setInt(3, price);
		ps.setInt(4, quantity);
		int result = ps.executeUpdate();
		ps.close();
		this.purchaseOrderItem.getConnection().close();
		return result;

	}

	public List<List<String>> getUserStatistics() throws SQLException {

		List<List<String>> result = new ArrayList<List<String>>();
		List<String> current = new ArrayList<String>();
		this.purchaseOrderItem = DatabaseConnection.getInstance();
		String query = "select PO.lname as lname, PO.fname as fname, sum(POItem.price*POItem.quantity) as total, Address.zip as zip from (PO join POItem on PO.orderid=POItem.orderid) join Address on Address.cid=PO.cid group by PO.fname, PO.lname order by total desc";
		PreparedStatement ps = this.purchaseOrderItem.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			// current.add("**** ***");
			current.add(rs.getString("fname") + rs.getString("lname"));
			current.add(rs.getString("zip"));
			current.add(rs.getString("total"));
			result.add(current);
			current = new ArrayList<String>();
		}

		rs.close();
		ps.close();
		this.purchaseOrderItem.getConnection().close();
		return result;
	}

	public ArrayList<List<String>> getBooksSoldEachMonth(String date) throws SQLException {

		String solddate;
		String quantity;
		String title;
		ArrayList<List<String>> arr = new ArrayList<List<String>>();
		List<String> l;
		this.purchaseOrderItem = DatabaseConnection.getInstance();

		String query = "select  concat(year(date), '-',  month(date)) as dates, sum(quantity) as quantity, title from PO p , POItem item, Book b where concat(year(date), '-',  month(date))='"
				+ date + "'"
				+ "AND p.orderid = item.orderid AND item.bid = b.bid GROUP BY dates, title ORDER BY quantity desc";
		PreparedStatement ps = this.purchaseOrderItem.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
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

	public LinkedHashMap<String, Integer> getTopTenAllTime() throws SQLException {

		LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();
		// "SELECT bid, sum(quantity) AS num FROM bookstore.PO where status != "DENIED"
		// GROUP BY bid ORDER BY num DESC"
		this.purchaseOrderItem = DatabaseConnection.getInstance();
		String query = "SELECT title, POItem.bid as bid, sum(quantity) AS num FROM POItem join Book on POItem.bid=Book.bid GROUP BY bid ORDER BY num DESC LIMIT 10";

		PreparedStatement ps = this.purchaseOrderItem.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("title");
			Integer count = rs.getInt("num");
			list.put(bookid, count);
		}
		return list;
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
		this.purchaseOrderItem = DatabaseConnection.getInstance();
		String query = "SELECT * FROM PO,POItem WHERE PO.orderid=POItem.orderid AND POItem.bid = ?";
		PreparedStatement ps = this.purchaseOrderItem.getConnection().prepareStatement(query);
		ps.setString(1, bid);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			orderId = rs.getString("PO.orderid");
			lname = rs.getString("lname");
			fname = rs.getString("fname");
			status = rs.getString("status");
			customerId = rs.getInt("cid");
			date = rs.getString("date");
			quantity = rs.getInt("quantity");
			unitPrice = rs.getInt("price");
			OrderBean order = new OrderBean(orderId, quantity, unitPrice, lname, fname, status, customerId, date,
					rs.getString("bid"), "");
			orderList.add(order);

		}

		rs.close();
		ps.close();
		this.purchaseOrderItem.getConnection().close();

		return orderList;

	}

}
