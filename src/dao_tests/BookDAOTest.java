package dao_tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import DAO.BookDAO;
import bean.BookBean;
import DAO.DatabaseConnection;

public class BookDAOTest {

	
	@Test
	public void testConnection() throws SQLException {
	    Connection con = DatabaseConnection.getInstance().getConnection();
        assertNotNull(con);
        assertTrue(con.isValid(0));
        con.close();
		
		
	}
	
	@Test
	public void testRetreiveBook() throws SQLException {
		BookDAO bookDb =  new BookDAO();
		BookBean book= null;
		// Book inside Book db
		String bookId = "121-3-3434-4545-5"; 
		book = bookDb.retrieveBook(bookId);
		assertEquals(bookId,book.getBid());
		
	}

	@Test 
	public void testretrieveBookFromCategory() throws SQLException {
		BookDAO bookDb =  new BookDAO();
		List<BookBean> categoryBooks= null;
		String category =  "Science";
		categoryBooks = bookDb.retriveBookFromCategory(category);
		
		for(int i = 0; i<= categoryBooks.size()-1; i++) {
			
			   assertEquals(categoryBooks.get(i).getCategory(), category);
		}
		
		
		
	}
	
	

	

	
	

}
