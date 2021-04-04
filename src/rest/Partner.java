package rest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

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
	public String getProductInfo(@QueryParam("email") String email, @QueryParam("password") String password,
			@QueryParam("productId") String productId)
			throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		String product = null;

		BookStoreModel model = BookStoreModel.getInstance();
		UserBean partner = model.isUserExist(email, password);
		
		if (partner != null && partner.getUserType() == 1) {
			product = model.getProductInfo(productId);
			System.out.println("GGG");
		}
		
		else {
			product = model.jsonErrorMessage();
		}

		return product;

	}

	@GET
	@Path("/read/getOrdersByPartNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOrdersByPartNumber(@QueryParam("email") String email, @QueryParam("password") String password,
			@QueryParam("productId") String productId)
			throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		String order = null;
		BookStoreModel model = BookStoreModel.getInstance();
		UserBean partner = model.isUserExist(email, password);
		
		if (partner != null && partner.getUserType() == 1) {
			order = model.getOrdersByPartNumber(productId);
		}

		else {

			order = model.jsonErrorMessage();

		}

		return order;

	}
}
