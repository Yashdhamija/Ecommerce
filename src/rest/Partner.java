package rest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import bean.UserBean;
import model.BookStoreModel;

@Path("partner")
public class Partner {

	@GET
	@Path("/read/getProductInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProductInfo(@QueryParam("productId") String productId)
			throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		String product = null;
		BookStoreModel model = BookStoreModel.getInstance();
	    
		
		if (model.retrieveInfoOfBook(productId) != null) {
			product = model.getProductInfo(productId);
			

		} else {
			product = model.jsonErrorMessage();

		}
		


		return product;

	}
	

	@GET
	@Path("/read/getOrdersByPartNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOrdersByPartNumber(@QueryParam("productId") String productId)
			throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		String order = null;
		BookStoreModel model = BookStoreModel.getInstance();
		
		
		if (model.retrieveInfoOfBook(productId) != null) {
			order = model.getOrdersByPartNumber(productId);
		}

		else {
			order = model.jsonErrorMessage();
		}

		return order;
	}
}
