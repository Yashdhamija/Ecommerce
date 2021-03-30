package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import DAO.DAO;
import bean.AddressBean;
import bean.BookBean;
import bean.CartBean;
import bean.OrderBean;
import bean.ReviewBean;
import bean.UserBean;

public class BookStoreModel {

	private static BookStoreModel instance;
	private DAO dao;
	private ArrayList<Integer> orderNumber = null;
	private int paymentCounter;

	public BookStoreModel() {

		this.dao = new DAO();
		this.orderNumber = new ArrayList<Integer>();
		this.paymentCounter = 0;
	}

	public static BookStoreModel getInstance() throws ClassNotFoundException {
		if (instance == null) {
			instance = new BookStoreModel();
			instance.dao = new DAO();
		}
		return instance;
	}

	public int insertUserLogin(String fname, String lname, String email, String password) throws SQLException {

		return this.dao.insertUserDB(fname, lname, email, password);

	}

	public int insertPartnerLogin(String email, String password, String fname, String lname) throws SQLException {

		return this.dao.insertPartnerDB(email, password, fname, lname);

	}

	public int insertAReview(String fname, String lname, String bid, String review, String title) throws SQLException {
		return this.dao.insertReview(fname, lname, bid, review, title);
	}

	public int insertIntoAddress(String street, String province, String country, String zip, String phone, String city)
			throws SQLException {
		return this.dao.insertAddress(street, province, country, zip, phone, city);
	}

	public void insertPO(int orderId, String fname, String lname, String status, String email) throws SQLException {
		this.dao.insertPurchaseOrder(orderId, fname, lname, status, email);
	}

	public int insertPOItem(int orderId, String bid, int price, int quantity) throws SQLException {

		return this.dao.insertPurchaseOrderItem(orderId, bid, price, quantity);

	}

	public int insertAdmin(String email, String lname, String fname, String password) throws SQLException {

		return this.dao.insertAdminIntoDB(email, fname, lname, password);
	}

	public boolean isVisitorExist(String email, String password) throws NoSuchAlgorithmException {
		return this.dao.IsVisitorExistInDB(email, this.encryptPassword(password));

	}

	public boolean isPartnerExist(String email, String password) throws NoSuchAlgorithmException {
		return this.dao.IsPartnerExistInDB(email, this.encryptPassword(password));
	}

	public String getPassword(String password) {
		return this.dao.retrievePassword(password);
	}

	public String getPartnerEmail(String uid) {
		return this.dao.retrievePartnerEmail(uid);
	}

	public String getCustomerName(String email) {

		return this.dao.getCustomerName(email);
	}

	public String getPartnerName(String email) {

		return this.dao.getPartnerName(email);
	}

	public List<BookBean> retrieveBookRecords(String bid) throws SQLException {
		return this.dao.retreivebookrecord(bid);
	}

	public List<BookBean> retrieveInfoOfBook(String bid) throws SQLException {
		return this.dao.retrievebookinfo(bid);
	}

	public List<BookBean> retrieveBooksUsingCategory(String category) throws SQLException {
		return this.dao.retriveBookFromCategory(category);
	}

	public List<ReviewBean> retrieveLastThreeReviews(String bid) throws SQLException {
		return this.dao.retriveReviews(bid);
	}

	public int retrievePriceofABook(String bid) throws SQLException {
		return this.dao.retrievePriceofSingleBook(bid);
	}

	public String retrieveBookTitle(String bid) throws SQLException {
		return this.dao.retrieveSingleBookTitle(bid);
	}

	public AddressBean retrieveAddress(String email) throws SQLException {
		return this.dao.retrieveAddressById(email);
	}

	public UserBean retrieveUserInfo(String email) throws SQLException {
		return this.dao.retrieveAllUserInfo(email);
	}

	public UserBean retrievePartnerInfo(String email) throws SQLException {

		return this.dao.RetrievePartnerInfo(email);

	}

	public List<BookBean> getSearchedBook(String title) throws SQLException {
		return this.dao.retreivebook(title);
	}

	public String searchResultsCount(String title) throws SQLException {
		return this.dao.numberOfSearchResults(title);
	}

	public String encryptPassword(String password) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		String encryptedpassword = Base64.getEncoder().encodeToString(hashed);

		return encryptedpassword;
	}

	// Cart methods

	// This method returns the total in the cart. This method should be double**
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
	public Map<String,CartBean> quantityUpdate(Map<String,CartBean> cart, int quantity, String bid) {
	
		cart.get(bid).setQuantity(quantity);
		return cart;
	}

	public int getIncrementCounter() {

		return this.paymentCounter;
	}

	public void incrementPaymentCounter() {

		this.paymentCounter++;
	}

	public void resetPaymentCounter() {

		this.paymentCounter = 0;
	}

	public int OrderNumberGenerator() {
		Random random = new Random();
		int number =  random.nextInt(1000000);
		System.out.println("This is the generateed number"+ number);
		//int number = (int) (Math.random() * 1000000);
		int orderNum = 0;

		if (!this.orderNumber.contains(number)) {

			this.orderNumber.add(number);
			orderNum = number;
			return orderNum;
		}

		return orderNum;

	}

	public String getProductInfo(String productId) throws SQLException {
		// TODO validate product id
		BookBean book = this.instance.dao.getProductJSON(productId);
		JsonObjectBuilder bookJSON = Json.createObjectBuilder();
		bookJSON.add("BookTitle", book.getBid()).add("Title", book.getTitle()).add("Price", book.getPrice())
				.add("Category", book.getCategory());
		JsonObject value = bookJSON.build();
		String serializedBookJson = value.toString();
		return serializedBookJson;
	}
	
	
	public String jsonErrorMessage() {
		JsonObjectBuilder bookJSON = Json.createObjectBuilder();
		bookJSON.add("Error Message:", "This information cannot be accessed.");
		JsonObject value = bookJSON.build();
		String serializedBookJson = value.toString();
		return serializedBookJson;
	}
		
		
	public	boolean isValidAdmin(String email,String password) throws NoSuchAlgorithmException {
		
		
		return this.dao.IsAdminValidated(email, this.encryptPassword(password));
		
	}
	

	public String getOrdersByPartNumber(String productId) throws SQLException {
		List<OrderBean> orders = this.instance.dao.getOrdersByPartNumber(productId);

		JsonObjectBuilder doc = Json.createObjectBuilder();
		JsonArrayBuilder result = Json.createArrayBuilder();
		for (OrderBean order : orders) {
			result.add(Json.createObjectBuilder().add("orderId", order.getOrderId())
					.add("name", order.getFname() + order.getLname()).add("status", order.getStatus())
					.add("address", order.getAddress()).add("Date", order.getDate())
					.add("quantity", order.getQuantity()).add("unitPrice", order.getPrice()));
		}
		doc.add("productId", productId).add("orders:", result);
		JsonObject obj = doc.build();
		return obj.toString();
	}

//	public boolean isValidAdmin(String email, String password) {
//		String dbEmail = this.dao.getAdminEmail(email);
//		String dbPassword = this.dao.getAdminPwd(password);
//		System.out.println("The email is" + dbEmail);
//		System.out.println("The password is" + dbPassword);
//
//		if (dbEmail != null && dbPassword != null & dbEmail.equals(email) && dbPassword.equals(password)) {
//
//			return true;
//		}
//
//		else {
//
//			return false;
//		}
//
//	}

	public LinkedHashMap<String, Integer> retrieveTopTenAllTime() throws SQLException {
		return this.dao.getTopTenAllTime();
	}

	public LinkedHashMap<String, LinkedHashMap<String, Integer>> retrieveBooksSoldEachMonth() throws SQLException {
		return this.dao.getBooksSoldEachMonth();
	}

	public List<List<String>> retrieveUserStatistics() throws SQLException {
		return this.dao.getUserStatistics();
	}

}
