package model;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import DAO.AdminDAO;
import DAO.PODAO;
import DAO.POItemDAO;
import DAO.ReviewDAO;
import bean.ReviewBean;
import security.PasswordEncryption;

public class AdminService {
	
	private static AdminService instance;
	private AdminDAO admin;
	private POItemDAO poitem;
	private ReviewDAO review;
	private PODAO po;
	
	public AdminService() throws SQLException {
		this.admin = new AdminDAO();
		this.poitem = new POItemDAO();
		this.review = new ReviewDAO();
		this.po = new PODAO();
	}
	
	public static AdminService getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new AdminService();
		}
		return instance;
	}
	
	public void insertAdmin(String email, String lname, String fname, String password) throws SQLException, NoSuchAlgorithmException {

		this.admin.insertAdminIntoDB(email, fname, lname, PasswordEncryption.encryptPassword(password));
	}
	
	public boolean isValidAdmin(String email, String password) throws NoSuchAlgorithmException {

		 return this.admin.IsAdminValidated(email, PasswordEncryption.encryptPassword(password));
	}
	
	public List<List<String>> retrieveUserStatistics() throws SQLException {
		//return this.dao.getUserStatistics();
		 return this.poitem.getUserStatistics();
	}
	
	public List<ReviewBean> retrieveAllReviews() throws SQLException {
		 return this.review.retriveAllReviews();
	}
	
	public ArrayList<String> getAllDates() throws SQLException {
		return this.po.getDates();
	}
	
	public void deleteAReview(String bid, String review) {
		//this.dao.deleteReview(bid, review);
		  this.review.deleteReview(bid, review);
	}
	
	public LinkedHashMap<String, Integer> retrieveTopTenAllTime() throws SQLException {
		//return this.dao.getTopTenAllTime();
		 return this.poitem.getTopTenAllTime();
	}

	public ArrayList<List<String>> retrieveBooksSoldEachMonth(String date) throws SQLException {
		//return this.dao.getBooksSoldEachMonth(date);
		 return this.poitem.getBooksSoldEachMonth(date);
	}

}
