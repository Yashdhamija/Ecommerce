package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ReviewBean;

public class ReviewDAO {
	
	private DatabaseConnection review;

	public ReviewDAO() throws SQLException {
	}
	
	
	public int insertReview(String fname, String lname, String bid, String review, String title) throws SQLException {

		String query = "INSERT INTO Review VALUES(?,?,?,?,?,?,?)";
		this.review = DatabaseConnection.getInstance();
		PreparedStatement ps = this.review.getConnection().prepareStatement(query);

		ps.setString(1, fname);
		ps.setString(2, lname);
		ps.setString(3, bid);
		ps.setString(4, review);
		ps.setString(5, null);
		ps.setString(6, title);
		ps.setInt(7, 0);
		
		int result = ps.executeUpdate();
		
		ps.close();
		this.review.getConnection().close();
		return result;

	}
	
	public List<ReviewBean> retriveReviews(String bid) throws SQLException {
				
		List<ReviewBean> list = new ArrayList<ReviewBean>();

		String query = "SELECT * FROM Review WHERE bid='" + bid + "' ORDER BY reviewid DESC limit 3";
		
		this.review = DatabaseConnection.getInstance();
		PreparedStatement ps = this.review.getConnection().prepareStatement(query);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String bookid = rs.getString("bid");
			String review = rs.getString("review");
			String title = rs.getString("title");
			int rating = rs.getInt("rating");
			list.add(new ReviewBean(fname, lname, bookid, review, title, rating));

		}

		rs.close();
		ps.close();
		this.review.getConnection().close();
		return list;

	}
	
	
	public List<ReviewBean> retriveAllReviews() throws SQLException {

		this.review = DatabaseConnection.getInstance();
		
		List<ReviewBean> list = new ArrayList<ReviewBean>();
		String query = "SELECT * FROM Review";
		PreparedStatement ps = this.review.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String bookid = rs.getString("bid");
			String review = rs.getString("review");
			String title = rs.getString("title");
			int rating = rs.getInt("rating");
			list.add(new ReviewBean(fname, lname, bookid, review, title, rating));

		}

		rs.close();
		ps.close();
		this.review.getConnection().close();
		return list;

	}
	
	
	public void deleteReview(String bid, String review) {
		
		try {
			this.review = DatabaseConnection.getInstance();
			String query = "DELETE FROM Review WHERE bid = ? AND review = ?";
			PreparedStatement ps = this.review.getConnection().prepareStatement(query);
			ps.setString(1, bid);
			ps.setString(2,review);
			int rs = ps.executeUpdate();			
			this.review.getConnection().close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
