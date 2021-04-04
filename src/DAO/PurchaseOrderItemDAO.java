package DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PurchaseOrderItemDAO {
	
	
	
	private static DatabaseConnection purchaseOrderItem;
	private UserDAO user;

	public PurchaseOrderItemDAO() throws SQLException {

		this.purchaseOrderItem = DatabaseConnection.getInstance();
		this.user = new UserDAO();

	}
	
	
	
	
	
	
	
	
	
	
	
	public int insertPurchaseOrderItem(int orderId, String bid, int price, int quantity) throws SQLException {

		// int orderId = this.retrieveOrderIdFromPO(email);
		System.out.println("This is the orderId from POItem" + orderId);
	
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

	
	

	
	
	

}
