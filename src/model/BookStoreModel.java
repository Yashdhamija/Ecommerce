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
import java.util.UUID;

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

import DAO.BookDAO;
import DAO.AddressDAO;
import DAO.AdminDAO;
import DAO.PODAO;
import DAO.POItemDAO;
import DAO.ReviewDAO;
import DAO.UserDAO;


public class BookStoreModel {

	private static BookStoreModel instance;
	private DAO dao;
	private ArrayList<Integer> orderNumber = null;
	private int paymentCounter;
	
	private BookDAO book;
	private AddressDAO address;
	private AdminDAO admin;
	private PODAO po;
	private POItemDAO poitem;
	private ReviewDAO review;
	private UserDAO user;
	

	public BookStoreModel() throws SQLException {

		this.dao = new DAO();
		
		// New DAO
		this.book =  new BookDAO();
		this.address = new AddressDAO();
		this.admin = new AdminDAO();
		this.po = new PODAO();
		 this.poitem = new POItemDAO();
		 this.review = new ReviewDAO();
		this.user =  new UserDAO();
		////
		this.orderNumber = new ArrayList<Integer>();
		this.paymentCounter = 0;
	}

	public static BookStoreModel getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new BookStoreModel();
			instance.dao = new DAO();
		}
		return instance;
	}

	public int insertUserLogin(String fname, String lname, String email, String password) throws SQLException {

		//return this.dao.insertUserDB(fname, lname, email, password);
        return this.user.insertUserDB(fname, lname, email, password);
	}

	public void insertPartnerLogin(String email, String password, String fname, String lname) throws SQLException, NoSuchAlgorithmException {
		this.dao.insertPartnerDB(fname, lname, email, password);
		this.dao.insertPartnerKey(email);

	}
	
	public String getpartnerKey(String email) {
		return this.dao.getpartnerKey(email);
	}
	
	public boolean isValidPartnerKey(String key) {
		return this.dao.isValidPartnerKey(key);
	}


	public int insertAReview(String fname, String lname, String bid, String review, String title, int rating) throws SQLException {
		return this.dao.insertReview(fname, lname, bid, review, title, rating);
	}

	public int insertIntoAddress(String email, String street, String province, String country, String zip, String phone,
			String city) throws SQLException {
		//return this.dao.insertAddress(email, street, province, country, zip, phone, city);
		return this.address.insertAddress(email, street, province, country, zip, phone, city);
	}

	public void insertPO(int orderId, String fname, String lname, String status, String email) throws SQLException {
		//this.dao.insertPurchaseOrder(orderId, fname, lname, status, email);
		this.po.insertPurchaseOrder(orderId, fname, lname, status, email);
	}

	public int insertPOItem(int orderId, String bid, int price, int quantity) throws SQLException {

		//return this.dao.insertPurchaseOrderItem(orderId, bid, price, quantity);
		return this.poitem.insertPurchaseOrderItem(orderId, bid, price, quantity);
	}

	public void insertAdmin(String email, String lname, String fname, String password) throws SQLException {

		//this.dao.insertAdminIntoDB(email, fname, lname, password);
		this.admin.insertAdminIntoDB(email, fname, lname, password);
	}
	
	public boolean isEmailTaken(String email) {
		//return this.dao.isEmailTaken(email);
		return this.user.isEmailTaken(email);
	}
	
	public UserBean isUserExist(String email, String password) throws NoSuchAlgorithmException {
		//return this.dao.isUserExist(email, this.encryptPassword(password));
		 return this.user.isUserExist(email, this.encryptPassword(password));
	}

	public String getPassword(String password) {
		//return this.dao.retrievePassword(password);
		return this.user.retrievePassword(password);
	}

	public String getCustomerName(String email) {

		//return this.dao.getCustomerName(email);
		return this.user.getCustomerName(email);
	}

	public String getPartnerName(String email) {

		//return this.dao.getPartnerName(email);
		 return this.user.getPartnerName(email);
	}

	public List<BookBean> retrieveBookRecords() throws SQLException {
		//return this.dao.retrieveAllBooks();
		 return this.book.retrieveAllBooks();
	}

	public BookBean retrieveInfoOfBook(String bid) throws SQLException {
		//return this.dao.retrievebookinfo(bid);
		return this.book.retrievebookinfo(bid);
	}

	public List<BookBean> retrieveBooksUsingCategory(String category) throws SQLException {
		//return this.dao.retriveBookFromCategory(category);
		 return this.book.retriveBookFromCategory(category);
	}

	public List<ReviewBean> retrieveLastThreeReviews(String bid) throws SQLException {
		//return this.dao.retriveReviews(bid);
		 return this.review.retriveReviews(bid);
	}
	
	public List<ReviewBean> retrieveAllReviews() throws SQLException {
		//return this.dao.retriveAllReviews();
		 return this.review.retriveAllReviews();
	}
	
	
	

	public int retrievePriceofABook(String bid) throws SQLException {
		//return this.dao.retrievePriceofSingleBook(bid);
		 return this.book.retrievePriceofSingleBook(bid);
	}

	public String retrieveBookTitle(String bid) throws SQLException {
		//return this.dao.retrieveSingleBookTitle(bid);
		 return this.book.retrieveSingleBookTitle(bid);
	}

	public AddressBean retrieveAddress(String email) throws SQLException {
		//return this.dao.retrieveAddressByEmail(email);
		return this.address.retrieveAddressByEmail(email);
	}	  

	public UserBean retrieveUserInfo(String email) throws SQLException {
		//return this.dao.retrieveAllUserInfo(email);
		return this.user.retrieveAllUserInfo(email);
	}

	public String retrieveBookUrl(String bid) throws SQLException {
		//return this.dao.retrieveUrlOfSingleBook(bid);
		return this.book.retrieveUrlOfSingleBook(bid);
	}

	public List<BookBean> getSearchedBook(String title) throws SQLException {
		//return this.dao.retreivebook(title);
		 return this.book.retreivebook(title);
	}

	public String encryptPassword(String password) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		String encryptedpassword = Base64.getEncoder().encodeToString(hashed);

		return encryptedpassword;
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

	public String getProductInfo(String productId) throws SQLException {
		BookBean book = this.instance.dao.getProductJSON(productId);
		JsonObjectBuilder bookJSON = Json.createObjectBuilder();
		bookJSON.add("BookId", book.getBid()).add("Title", book.getTitle()).add("Price", book.getPrice())
				.add("Category", book.getCategory());
		JsonObject value = bookJSON.build();
		String serializedBookJson = value.toString();
		return serializedBookJson;
	}

	public String jsonErrorMessage() {
		JsonObjectBuilder bookJSON = Json.createObjectBuilder();
		bookJSON.add("Error Message:", "This information cannot be accessed. Please provide a valid product Id!");
		JsonObject value = bookJSON.build();
		String serializedBookJson = value.toString();
		return serializedBookJson;
	}

	public boolean isValidAdmin(String email, String password) throws NoSuchAlgorithmException {

		//return this.dao.IsAdminValidated(email, this.encryptPassword(password));
		 return this.admin.IsAdminValidated(email, this.encryptPassword(password));
	}
	
	public AddressBean getUserAddress(int cid) throws SQLException {
		//return this.dao.retrieveAddressByCustomerId(cid);
		  return this.address.retrieveAddressByCustomerId(cid);
		
	}
	
	public BookBean getSingleBookInfo(String bid) throws SQLException {
		
		//return this.dao.retrieveBook(bid);
		 return this.book.retrieveBook(bid);
	}

	public String getOrdersByPartNumber(String productId) throws SQLException {
		List<OrderBean> orders = this.instance.dao.getOrdersByPartNumber(productId);

		JsonObjectBuilder doc = Json.createObjectBuilder();
		JsonArrayBuilder result = Json.createArrayBuilder();
		for (OrderBean order : orders) {
			
			AddressBean userAddress = this.getUserAddress(order.getCid());
			BookBean userbook =  this.getSingleBookInfo(productId);					
			
			result.add(Json.createObjectBuilder().add("orderDate", order.getDate())
					.add("name", order.getFname() + order.getLname())
					.add("street", userAddress.getStreet()).add("city", userAddress.getCity())
					.add("Province", userAddress.getProvince()).add("zipcode", userAddress.getZip())
					.add("street", userAddress.getStreet()).add("book",
							Json.createObjectBuilder().add("title", userbook.getTitle())
									.add("bookId", userbook.getBid()).add("category", userbook.getCategory())
									.add("quantity", order.getQuantity()).add("price", order.getPrice())));
				
		}
		doc.add("productId", productId).add("orders:", result);
		JsonObject obj = doc.build();
		return obj.toString();
	}


	public LinkedHashMap<String, Integer> retrieveTopTenAllTime() throws SQLException {
		//return this.dao.getTopTenAllTime();
		 return this.poitem.getTopTenAllTime();
	}

	public ArrayList<List<String>> retrieveBooksSoldEachMonth(String date) throws SQLException {
		//return this.dao.getBooksSoldEachMonth(date);
		 return this.poitem.getBooksSoldEachMonth(date);
	}
	
	public ArrayList<String> getAllDates() throws SQLException {
		//return this.dao.getDates();
		return this.po.getDates();
	}
	
	public ArrayList<String> getAllUserOrderDates(String email) throws SQLException {
		//return this.dao.getAllUserOrderDates(email);
		return this.po.getAllUserOrderDates(email);
	}
	
	public List<OrderBean> getOrderByDate(String date, String email) {
		//return this.dao.getOrderByDate(date, email);
		return this.po.getOrderByDate(date, email);
	}

	public List<List<String>> retrieveUserStatistics() throws SQLException {
		//return this.dao.getUserStatistics();
		 return this.poitem.getUserStatistics();
	}
	
	public void deleteAReview(String bid, String review) {
		//this.dao.deleteReview(bid, review);
		  this.review.deleteReview(bid, review);
	}

}
