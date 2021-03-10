package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import DAO.DAO;
import bean.BookBean;
import bean.ReviewBean;

public class BookStoreModel {

	private DAO dao;

	public BookStoreModel() {

		this.dao = new DAO();

	}

	public void insertUserLogin(String fname, String lname, String email, String password) {

		this.dao.insertUserDB(fname, lname, email, password);

	}

	public void insertPartnerLogin(int uid, String password) {

		this.dao.insertPartnerDB(uid, password);

	}
	
	public void insertAReview(String fname, String lname, String bid, String review) throws SQLException{
		this.dao.insertReview(fname, lname, bid, review);
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
	
	public List<BookBean> retrieveInfoOfBook(String bid) throws SQLException{
		return this.dao.retrievebookinfo(bid);
	}
	
	public List<BookBean> retrieveBooksUsingCategory(String category) throws SQLException{
		return this.dao.retriveBookFromCategory(category);
	}
	
	public List<ReviewBean> retrieveLastTwoReviews(String bid) throws SQLException{
		return this.dao.retriveReviews(bid);
	}
	

	
	public List<BookBean> getSearchedBook(String title) throws SQLException{
		return this.dao.retreivebook(title);
	}
	
	public String searchResultsCount(String title) throws SQLException {
		return this.dao.numberOfSearchResults(title);
	}
	
	
	
	

}
