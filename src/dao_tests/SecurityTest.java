package dao_tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import DAO.BookDAO;
import bean.BookBean;
import security.HtmlFilter;

public class SecurityTest {


	
	@Test // SQL injection test
	public void testWrongParameterSetRetreiveBook() throws SQLException {
		BookDAO bookDb =  new BookDAO();
		BookBean book= null;
		// Book inside Book db
		String bookId = "SELECT * FROM Book WHERE bid=?"; 
		book = bookDb.retrieveBook(bookId);
		assertNull(book);
		
	}
	
	
	@Test //Cross-Site Scripting Test
	public void testRemovalOfHTMLTag() throws SQLException {
		String readerComments = "<script>alert(“Attack”)</script>";
		String updatedReaderComments = HtmlFilter.removeHTMLTags(readerComments);
		System.out.println(updatedReaderComments);
		assertEquals(updatedReaderComments,"");
		
	}
	
	@Test //Cross-Site Scripting Test
	public void testReplaceOfHTMLTag() throws SQLException {
		String readerComments = "<script>alert(“Attack”)</script>";
		String updatedReaderComments = HtmlFilter.replaceHTMLTags(readerComments);
		System.out.println(updatedReaderComments);
		assertEquals(updatedReaderComments,"&ltscript>alert(“Attack”)&lt/script>");
		
	}
	
	
	

}
