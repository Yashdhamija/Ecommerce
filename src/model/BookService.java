package model;

import java.sql.SQLException;
import java.util.List;

import DAO.BookDAO;
import DAO.ReviewDAO;
import bean.BookBean;
import bean.ReviewBean;

public class BookService {
	
	private static BookService instance;
	private BookDAO book;
	private ReviewDAO review;
	
	public BookService() throws SQLException {;
		this.book =  new BookDAO();
		this.review = new ReviewDAO();
	}
	
	public static BookService getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new BookService();
		}
		return instance;
	}
	
	public int insertAReview(String fname, String lname, String bid, String review, String title, int rating) throws SQLException {
		return this.review.insertReview(fname, lname, bid, review, title, rating);
	}
	
	public List<BookBean> retrieveBookRecords() throws SQLException {
		 return this.book.retrieveAllBooks();
	}

	public BookBean retrieveInfoOfBook(String bid) throws SQLException {
		return this.book.retrievebookinfo(bid);
	}

	public List<BookBean> retrieveBooksUsingCategory(String category) throws SQLException {
		 return this.book.retriveBookFromCategory(category);
	}

	public List<ReviewBean> retrieveLastThreeReviews(String bid) throws SQLException {
		 return this.review.retriveReviews(bid);
	}
	
	public int retrievePriceofABook(String bid) throws SQLException {
		 return this.book.retrievePriceofSingleBook(bid);
	}

	public String retrieveBookTitle(String bid) throws SQLException {
		 return this.book.retrieveSingleBookTitle(bid);
	}
	
	public String retrieveBookUrl(String bid) throws SQLException {
		return this.book.retrieveUrlOfSingleBook(bid);
	}

	public List<BookBean> getSearchedBook(String title) throws SQLException {
		 return this.book.retreivebook(title);
	}
	
	public BookBean getSingleBookInfo(String bid) throws SQLException {
				 return this.book.retrieveBook(bid);
	}

}





