package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import DAO.PODAO;
import DAO.POItemDAO;
import bean.CartBean;
import bean.OrderBean;

public class OrderService {
	
	private static OrderService instance;
	private PODAO po;
	private POItemDAO poitem;
	private ArrayList<Integer> orderNumber = null;
	
	public OrderService() throws SQLException {
		this.po = new PODAO();
		this.poitem = new POItemDAO();
		this.orderNumber = new ArrayList<Integer>();
	}
	
	public static OrderService getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new OrderService();
		}
		return instance;
	}
	
	
	public void insertPO(int orderId, String fname, String lname, String status, String email) throws SQLException {
		this.po.insertPurchaseOrder(orderId, fname, lname, status, email);
	}

	public int insertPOItem(int orderId, String bid, int price, int quantity) throws SQLException {
		return this.poitem.insertPurchaseOrderItem(orderId, bid, price, quantity);
	}
	
	public int cartTotal(Map<String, CartBean> cart) {
		int total = 0;
		for (Map.Entry<String, CartBean> entry : cart.entrySet()) {

			CartBean book = entry.getValue();
			total += book.getPrice() * book.getQuantity();

		}
		return total;
	}

	public Map<String, CartBean> remove(String bid, Map<String, CartBean> cart) {

		cart.remove(bid);
		return cart;

	}

	//
	public Map<String, CartBean> quantityUpdate(Map<String, CartBean> cart, int quantity, String bid) {

		cart.get(bid).setQuantity(quantity);
		return cart;
	}


	public int OrderNumberGenerator() {
		Random random = new Random();
		int number = random.nextInt(1000000);
		System.out.println("This is the generateed number" + number);
		
		int orderNum = 0;

		if (!this.orderNumber.contains(number)) {

			this.orderNumber.add(number);
			orderNum = number;
			return orderNum;
		}

		return orderNum;

	}
	
	public ArrayList<String> getAllUserOrderDates(String email) throws SQLException {
		return this.po.getAllUserOrderDates(email);
	}
	
	public List<OrderBean> getOrderByDate(String date, String email) {
		return this.po.getOrderByDate(date, email);
	}

}
