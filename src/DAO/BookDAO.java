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
		
		this.book =   DatabaseConnection.getInstance();
		
	}
	
	
	
	
	
	public String retrieveSingleBookTitle(String bid) throws SQLException { // change this to the below method grabbing all information return bookbean and just get title
		String btitle = "";
	

		String query = "SELECT title FROM Book WHERE bid=?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			btitle = rs.getString("title");
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return btitle;
	

	}
	
	
	public List<BookBean> retrievebookinfo(String bid) throws SQLException {

		
		List<BookBean> list = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book WHERE bid=?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, bid);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookid = rs.getString("bid");
			String btitle = rs.getString("title");
			int bprice = Integer.parseInt(rs.getString("price"));
			String category = rs.getString("category");
			String url = rs.getString("imageurl");
			list.add(new BookBean(bookid, btitle, bprice, category, url));

		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return list;

	}

	
	
	
	
	public  BookBean retrieveBook(String bid) throws SQLException { // change this to the below method grabbing all information return bookbean and just get title
		BookBean book = null;
		

		String query = "SELECT * FROM Book WHERE bid=?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			book = new BookBean(rs.getString("bid"), 
					rs.getString("title"),
					rs.getInt("price"),
					rs.getString("category"),
					rs.getString("imageurl"));
			
			
		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return book;
	}
	
	
	public List<BookBean> retreivebookrecord(String bid) throws SQLException { // This is for search functionality improve this
		
		List<BookBean> l = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book WHERE bid LIKE ?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(1, "%" + bid + "%");
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
		System.out.println(l.get(0).getBid());
		return l;
	}
	
	
	public List<BookBean> retriveBookFromCategory(String category) throws SQLException {
	
		List<BookBean> list = new ArrayList<BookBean>();

		String query = "SELECT * FROM Book WHERE category=?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(4, category);
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
		String query = "SELECT imageurl FROM Book WHERE bid=?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			url = rs.getString("imageurl");

		}

		rs.close();
		ps.close();
		this.book.getConnection().close();
		return url;

	}
	
	
	
	public List<BookBean> numberOfSearchResults(String title) throws SQLException {
	
		List<BookBean> searchResult = new ArrayList<BookBean>();
		String s;
		String query = "SELECT * FROM Book WHERE title LIKE ?";

		PreparedStatement ps = this.book.getConnection().prepareStatement(query);
		ps.setString(2, "%" + title + "%");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String bookId = rs.getString("bid");
			String bookTitle = rs.getString("title");
			String bookCategory = rs.getString("category");
			String url = rs.getString("imageurl");
			int bookPrice = Integer.parseInt(rs.getString("price"));
			searchResult.add(new BookBean(bookId,bookTitle,bookPrice, bookCategory, url));

		}
		
		rs.close();
		ps.close();
		this.book.getConnection().close();
		return searchResult;
	}
	
	
	public BookBean getProductJSON(String productId) throws SQLException {

		return this.retrieveBook(productId);

	}




}