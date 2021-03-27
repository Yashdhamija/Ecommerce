package rest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.BookStoreModel;

@Path("partner")
public class Partner {
	
    @GET
    @Path("/read/getProductInfo")
    @Produces(MediaType.TEXT_PLAIN)
    public String getProductInfo(@QueryParam("email")String email,@QueryParam("password") String password, @QueryParam("productId")String productId) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
    	
    	 String product = null;
    	
    	BookStoreModel model = BookStoreModel.getInstance();
    	if(model.getPartnerPassword(model.encryptPassword(password)).equals("partner password exists") &&
    			model.getPartnerEmail(email).equals("partner email exists")) {
    		product = model.getProductInfo(productId);
    		System.out.println("GGG");
    		 return product;
    		
    	}
    	else {
    		
    		return product;
    	}
		
     
    }
    
    @GET
    @Path("/read/getOrdersByPartNumber")
    @Produces(MediaType.TEXT_PLAIN)
	public String getOrdersByPartNumber(@QueryParam("email")String email,@QueryParam("password") String password, @QueryParam("productId")String productId) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
		
    	String order = null;
    	BookStoreModel model = BookStoreModel.getInstance();
    	
       	if(model.getPartnerPassword(model.encryptPassword(password)).equals("partner password exists") &&
    			model.getPartnerEmail(email).equals("partner email exists")) {
       		order = model.getOrdersByPartNumber(productId);
       		
       		
       	}
		
	    return order;
	}
}
