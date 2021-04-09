package rest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.BookService;
import model.UserService;
import model.WebService;

@Path("partner")
public class Partner {

	@GET
	@Path("/read/getProductInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProductInfo(@QueryParam("key") String key, @QueryParam("productId") String productId)
			throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		String product = null;
		WebService webService = WebService.getInstance();
		UserService userService = UserService.getInstance();
		BookService bookService = BookService.getInstance();
		
		if (userService.isValidPartnerKey(key)) {
			if (bookService.retrieveInfoOfBook(productId) != null) {
				product = webService.getProductInfo(productId);				
			} else {
				product = webService.jsonErrorMessage();
			}
		} else {
			product = webService.jsonAuthenticationErrorMessage();
		}
		
		return product;
	}
	

	@GET
	@Path("/read/getOrdersByPartNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOrdersByPartNumber(@QueryParam("key") String key, @QueryParam("productId") String productId)
			throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		String order = null;
		WebService webService = WebService.getInstance();
		UserService userService = UserService.getInstance();
		BookService bookService = BookService.getInstance();
		
		if (userService.isValidPartnerKey(key)) {
			if (bookService.retrieveInfoOfBook(productId) != null) {
				order = webService.getOrdersByPartNumber(productId);
			} else {
				order = webService.jsonErrorMessage();
			}
		} else {
			order = webService.jsonAuthenticationErrorMessage();
		}

		return order;
	}
}
