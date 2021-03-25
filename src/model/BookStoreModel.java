package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import DAO.DAO;
import bean.AddressBean;
import bean.BookBean;
import bean.CartBean;
import bean.ReviewBean;
import bean.UserBean;

public class BookStoreModel {

	private DAO dao;
	private ArrayList<Integer> orderNumber = null;
	private int paymentCounter;
	public BookStoreModel() {

		this.dao = new DAO();
		this.orderNumber = new ArrayList<Integer>();
		this.paymentCounter= 0;
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
	
	
	public int insertPOItem(String email, String bid, int price, int quantity) throws SQLException {
		
		return this.dao.insertPurchaseOrderItem(email, bid, price, quantity);
		
	}
	
	public String getPartnerPassword(String password) {
		return this.dao.retrievePartnerPassword(password);

	}

	public String getEmail(String email) {
		return this.dao.retrieveEmail(email);
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
	public int cartTotal(List<CartBean> l) {
		int total = 0;

		for (int i = 0; i < l.size(); i++) {
			total += l.get(i).getPrice() * l.get(i).getQuantity();
		}

		return total;
	}

	public List<CartBean> remove(String bid, List<CartBean> l) {

		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getBookid().equals(bid)) {
				l.remove(i);
			}
		}
		return l;
	}

	//
	public List<CartBean> quantityUpdate(List<CartBean> l, int quantity, String bid) {
		int newTotal = 0;
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getBookid().equals(bid)) {
				l.get(i).setQuantity(quantity);

			}
		}
		return l;
	}
	
	
	public int getIncrementCounter( ) {
		
		return this.paymentCounter;
	}
	
	public void incrementPaymentCounter() {
		
		this.paymentCounter++;
	}
	
	
	public void resetPaymentCounter() {
		
		this.paymentCounter = 0;
	}

	public int OrderNumberGenerator() {

		int random = (int) (Math.random() * 1000000);
		int orderNum = 0;

		if (!this.orderNumber.contains(random)) {

			this.orderNumber.add(random);
			orderNum = random;
			return orderNum;
		}

		return orderNum;

	}
	
	
	
	

}
