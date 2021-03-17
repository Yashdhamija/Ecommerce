package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DAO.DAO;
import bean.BookBean;
import bean.CartBean;
import bean.ReviewBean;

public class BookStoreModel {

	private DAO dao;
	

	public BookStoreModel() {

		this.dao = new DAO();

	}

	public void insertUserLogin(String fname, String lname, String email, String password) throws SQLException {

		this.dao.insertUserDB(fname, lname, email, password);

	}

	public void insertPartnerLogin(int uid, String password) throws SQLException {

		this.dao.insertPartnerDB(uid, password);

	}

	public void insertAReview(String fname, String lname, String bid, String review) throws SQLException {
		this.dao.insertReview(fname, lname, bid, review);
	}
	
	public void insertIntoAddress(String street, String province, String country, String zip, String phone, String city) throws SQLException {
		this.dao.insertAddress(street, province, country, zip, phone, city);
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

	public String getUID(String uid) {
		return this.dao.retrieveUID(uid);
	}

	public String getFullName(String email) {

		return this.dao.getFullName(email);
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

	public List<ReviewBean> retrieveLastTwoReviews(String bid) throws SQLException {
		return this.dao.retriveReviews(bid);
	}

	public int retrievePriceofABook(String bid) throws SQLException {
		return this.dao.retrievePriceofSingleBook(bid);
	}
	
	public String retrieveBookTitle(String bid) throws SQLException {
		return this.dao.retrieveSingleBookTitle(bid);
	}

	public List<BookBean> getSearchedBook(String title) throws SQLException {
		return this.dao.retreivebook(title);
	}

	public String searchResultsCount(String title) throws SQLException {
		return this.dao.numberOfSearchResults(title);
	}

	
	//Cart methods
	
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
	
	
	
	
	
	
}

