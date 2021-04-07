package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.BookBean;

public class BookDAO {

	private static DatabaseConnection book;

	public BookDAO() throws SQLException {

		this.book = DatabaseConnection.getInstance();

	}

	public int retrievePriceofSingleBook(String bid) throws SQLException {

		int price = 0;
		this.book = DatabaseConnection.getInstance();
		String query = "SELECT price FROM Book WHERE bid = ?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, bid);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			price = rs.getInt("price");
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return price;
	}

	public List<BookBean> retrieveAllBooks() throws SQLException { // This is for search functionality improve this

		List<BookBean> l = new ArrayList<BookBean>();
		this.book = DatabaseConnection.getInstance();
		String query = "SELECT * FROM Book";
		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = rs.getInt("price");
			String category = rs.getString("category");
			String url = rs.getString("imageurl");
			l.add(new BookBean(bookid, btitle, bprice, category, url));
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return l;
	}

	public String retrieveSingleBookTitle(String bid) throws SQLException { // change this to the below method grabbing
																			// all information return bookbean and just
																			// get title
		String btitle = "";
		this.book = DatabaseConnection.getInstance();
		String query = "SELECT title FROM Book WHERE bid = ?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, bid);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			btitle = rs.getString("title");
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return btitle;
	}

	public BookBean retrieveBook(String bid) throws SQLException { // change this to the below method grabbing all
																	// information return bookbean and just get title
		BookBean book = null;
		this.book = DatabaseConnection.getInstance();
		String query = "SELECT * FROM Book WHERE bid=?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, bid);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			book = new BookBean(rs.getString("bid"), rs.getString("title"), rs.getInt("price"),
					rs.getString("category"), rs.getString("imageurl"));
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return book;
	}

	public BookBean retrievebookinfo(String bid) throws SQLException {

		BookBean bookinfo = null;
		this.book = DatabaseConnection.getInstance();
		String query = "SELECT * FROM Book WHERE bid=?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, bid);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			bookinfo = new BookBean(rs.getString("bid"), rs.getString("title"), rs.getInt("price"),
					rs.getString("category"), rs.getString("imageurl"));
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return bookinfo;

	}

	public List<BookBean> retreivebook(String title) throws SQLException {

		List<BookBean> l = new ArrayList<BookBean>();
		this.book = DatabaseConnection.getInstance();
		String query = "SELECT * FROM Book WHERE title LIKE ?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, "%" + title + "%");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = Integer.parseInt(rs.getString("price"));
			String category = rs.getString("category");
			String url = rs.getString("imageurl");
			l.add(new BookBean(bookid, btitle, bprice, category, url));
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return l;
	}

	public List<BookBean> retriveBookFromCategory(String category) throws SQLException {
		;
		List<BookBean> list = new ArrayList<BookBean>();
		this.book = DatabaseConnection.getInstance();
		String query = "SELECT * FROM Book WHERE category = ?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, category);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = Integer.parseInt(rs.getString("price"));
			String cat = rs.getString("category");
			String url = rs.getString("imageurl");
			list.add(new BookBean(bookid, btitle, bprice, cat, url));

		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return list;

	}

	public String retrieveUrlOfSingleBook(String bid) throws SQLException {

		String url = null;
		this.book = DatabaseConnection.getInstance();
		String query = "SELECT imageurl FROM Book WHERE bid=?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, bid);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			url = rs.getString("imageurl");
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return url;
	}
	
	
	public BookBean getProductJSON(String productId) throws SQLException {
		return retrievebookinfo(productId);
	}


}
