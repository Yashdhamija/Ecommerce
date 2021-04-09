package model;

import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import DAO.BookDAO;
import DAO.POItemDAO;
import bean.AddressBean;
import bean.BookBean;
import bean.OrderBean;

public class WebService {
	
	private static WebService instance;
	private static UserService userService;
	private static BookService bookService;
	private BookDAO book;
	private POItemDAO poitem;

	
	public WebService() throws SQLException, ClassNotFoundException {
		this.book = new BookDAO();
		this.poitem = new POItemDAO();
		WebService.userService = UserService.getInstance();
		WebService.bookService = BookService.getInstance();
	}
	
	public static WebService getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new WebService();
		}
		return instance;
	}
	
	// rest api calls from partner
	public String getProductInfo(String productId) throws SQLException {
		BookBean book = this.instance.book.getProductJSON(productId);
		
		JsonObjectBuilder bookJSON = Json.createObjectBuilder();
		bookJSON.add("BookId", book.getBid()).add("Title", book.getTitle()).add("Price", book.getPrice())
				.add("Category", book.getCategory());
		JsonObject value = bookJSON.build();
		String serializedBookJson = value.toString();
		return serializedBookJson;
	}

	public String jsonErrorMessage() {
		JsonObjectBuilder bookJSON = Json.createObjectBuilder();
		bookJSON.add("Error Message:", "This information cannot be accessed. Please provide a valid product Id!");
		JsonObject value = bookJSON.build();
		String serializedBookJson = value.toString();
		return serializedBookJson;
	}
	
	public String jsonAuthenticationErrorMessage() {
		JsonObjectBuilder bookJSON = Json.createObjectBuilder();
		bookJSON.add("Error Message:", "Sorry we could not verify your unique access key. Please try Again!\n");
		JsonObject value = bookJSON.build();
		String serializedBookJson = value.toString();
		return serializedBookJson;
	}
	
	public String getOrdersByPartNumber(String productId) throws SQLException {
		List<OrderBean> orders = this.poitem.getOrdersByPartNumber(productId);

		JsonObjectBuilder doc = Json.createObjectBuilder();
		JsonArrayBuilder result = Json.createArrayBuilder();
		for (OrderBean order : orders) {
			AddressBean userAddress = WebService.userService.getUserAddress(order.getCid());
			BookBean userbook =  WebService.bookService.getSingleBookInfo(productId);	
			
			result.add(Json.createObjectBuilder().add("orderDate", order.getDate())
					.add("name", order.getFname() + order.getLname()));
			
			if (userAddress != null) {
				result.add(Json.createObjectBuilder().add("street", userAddress.getStreet()).add("city", userAddress.getCity())
					.add("Province", userAddress.getProvince()).add("zipcode", userAddress.getZip()));
			}
			
			if (userbook != null) {
				result.add(Json.createObjectBuilder().add("book", Json.createObjectBuilder().add("title", userbook.getTitle())
						.add("bookId", userbook.getBid()).add("category", userbook.getCategory())
						.add("quantity", order.getQuantity()).add("price", order.getPrice())));
			}
				
		}
		
		doc.add("productId", productId).add("orders:", result);
		JsonObject obj = doc.build();
		return obj.toString();
	}

}
