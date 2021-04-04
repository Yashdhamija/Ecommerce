package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ReviewBean;

public class ReviewDAO {

	private static DatabaseConnection review;

	public ReviewDAO() throws SQLException {

		this.review = DatabaseConnection.getInstance();

	}

	public int insertReview(String fname, String lname, String bid, String review, String title) throws SQLException {

		String query = "INSERT INTO Review VALUES(?,?,?,?,?,?)";

		PreparedStatement ps = this.review.getConnection().prepareStatement(query);

		ps.setString(1, fname);
		ps.setString(2, lname);
		ps.setString(3, bid);
		ps.setString(4, review);
		ps.setString(5, null);
		ps.setString(6, title);
		int result = ps.executeUpdate();
		ps.close();
		this.review.getConnection().close();
		return result;

	}

	public List<ReviewBean> retriveReviews(String bid) throws SQLException {

		List<ReviewBean> list = new ArrayList<ReviewBean>();

		String query = "SELECT * FROM Review WHERE bid=? ORDER BY reviewid DESC limit 3";

		PreparedStatement ps = this.review.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String bookid = rs.getString("bid");
			String review = rs.getString("review");
			String title = rs.getString("title");
			list.add(new ReviewBean(fname, lname, bookid, review, title));

		}

		rs.close();
		ps.close();
		this.review.getConnection().close();
		return list;

	}

}
