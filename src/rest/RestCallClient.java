package rest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import rest.Partner;
import DAO.BookDAO;
import bean.BookBean;
import model.WebService;

public class RestCallClient {
	public static void main(String[] args) throws ClassNotFoundException, NoSuchAlgorithmException, SQLException {
		Partner partner = new Partner();
		BookDAO book = new BookDAO();
		WebService webService = new WebService();
		
		// test case 1: rest api call using api key and specific book id against dao methods
		BookBean bookInfo = book.retrieveBook("111-0-3343-2656-3");
		String bookAsString = "{\"BookId\":\""+bookInfo.getBid()+"\",\"Title\":\""+bookInfo.getTitle()+"\",\"Price\":"+bookInfo.getPrice()+",\"Category\":\""+bookInfo.getCategory()+"\"}";
		String restResponse = partner.getProductInfo("ad9abc4b-b5e0-4a7a-a119-c24a62d9bf94", "111-0-3343-2656-3");
		if(bookAsString.equals(restResponse)) {
			System.out.println("Test case 1 passed and the DAO methods return the same values as the REST endpoint");
			System.out.println("DAO methods return " + bookAsString);
			System.out.println("REST endpoint returns " + restResponse);
		}
		else {
			System.out.println("Test case 1 failed, the DAO methods does not return the same values as the REST endpoint");
			System.out.println("DAO methods return " + bookAsString);
			System.out.println("REST endpoint returns " + restResponse);
		}
		
		// test case 2: rest api call against dao method with a different book id
		bookInfo = book.retrieveBook("111-0-6764-2654-3");
		bookAsString = "{\"BookId\":\""+bookInfo.getBid()+"\",\"Title\":\""+bookInfo.getTitle()+"\",\"Price\":"+bookInfo.getPrice()+",\"Category\":\""+bookInfo.getCategory()+"\"}";
		restResponse = partner.getProductInfo("ad9abc4b-b5e0-4a7a-a119-c24a62d9bf94", "111-0-6764-2654-3");
		if(bookAsString.equals(restResponse)) {
			System.out.println("Test case 2 passed and the DAO methods return the same values as the REST endpoint");
			System.out.println("DAO methods return " + bookAsString);
			System.out.println("REST endpoint returns " + restResponse);
		}
		else {
			System.out.println("Test case 2 failed, the DAO methods does not return the same values as the REST endpoint");
			System.out.println("DAO methods return " + bookAsString);
			System.out.println("REST endpoint returns " + restResponse);
		}
		
		// test case 3
		String orderResultFromREST = partner.getOrdersByPartNumber("ad9abc4b-b5e0-4a7a-a119-c24a62d9bf94", "111-0-3343-2656-3");
		String orderResultFromModel = webService.getOrdersByPartNumber("111-0-3343-2656-3");
		if(orderResultFromREST.equals(orderResultFromModel)) {
			System.out.println("Test case 3 passed and the Model methods return the same values as the REST endpoint");
			System.out.println("Model methods return " + orderResultFromREST);
			System.out.println("REST endpoint returns " + orderResultFromModel);
		}
		else {
			System.out.println("Test case 3 failed, the Model methods does not return the same values as the REST endpoint");
			System.out.println("Model methods return " + orderResultFromREST);
			System.out.println("REST endpoint returns " + orderResultFromModel);
		}
		
		// test case 4: Similar to test case 3 but using another partner's api key
		orderResultFromREST = partner.getOrdersByPartNumber("bc629434-da50-416f-aead-c1c492b88271", "111-0-3343-2656-3");
		orderResultFromModel = webService.getOrdersByPartNumber("111-0-3343-2656-3");
		if(orderResultFromREST.equals(orderResultFromModel)) {
			System.out.println("Test case 4 passed and the Model methods return the same values as the REST endpoint");
			System.out.println("Model methods return " + orderResultFromREST);
			System.out.println("REST endpoint returns " + orderResultFromModel);
		}
		else {
			System.out.println("Test case 4 failed, the Model methods does not return the same values as the REST endpoint");
			System.out.println("Model methods return " + orderResultFromREST);
			System.out.println("REST endpoint returns " + orderResultFromModel);
		}
		
	}
}
