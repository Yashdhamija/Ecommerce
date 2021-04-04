package services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.BookDAO;
import bean.BookBean;
import bean.CartBean;
import bean.UserBean;

public class CartServices {
	
	
	
	private BookDAO bookdB;
	private UserBean user; 
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HashMap<String, CartBean> cart;
	
	
	
	public CartServices(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		
		this.request = request;
		this.response = response;
		this.bookdB = new BookDAO();
		this.cart = new HashMap<String, CartBean>();
		this.request.getSession().setAttribute("shoppingcart", this.cart);
		
	}
	
	
	
	public int cartTotal(Map<String, CartBean> cart) {
		int total = 0;
		for (Map.Entry<String, CartBean> entry : cart.entrySet()) {

			CartBean book = entry.getValue();
			total += book.getPrice() * book.getQuantity();

		}
		return total;
	}
	
	
	
	public void displayCartPage() throws ServletException, IOException {

		if (this.request.getSession().getAttribute("shoppingcart") == null) {

			this.request.getSession().setAttribute("carttotal", "0");

		}

		this.request.getRequestDispatcher("/Cart.jspx").forward(this.request, this.response);

	}

	public void updateCartFromHomePage(CartBean item) {

		String bookId = item.getBookid();
		int cartTotal;
		if (this.cart.containsKey(bookId)) {

			this.cart.get(bookId).setQuantity(this.cart.get(bookId).getQuantity() + 1);
			cartTotal = cartTotal(this.cart);
			this.request.getSession().setAttribute("carttotal", cartTotal);
			this.request.getSession().setAttribute("quantity", cart.get(bookId).getQuantity() + 1);
			this.request.getSession().setAttribute("shoppingcart", cart);

		}

		else {
			this.cart.put(bookId, item);
			System.out.println("cart items are " + this.cart);
			cartTotal = cartTotal(this.cart);
			this.request.getSession().setAttribute("cartsize", cart.size());
			this.request.getSession().setAttribute("carttotal", cartTotal);
			this.request.getSession().setAttribute("shoppingcart", cart);
			this.request.getSession().setAttribute("iscart", "clicked");

		}

	}
	
	public void removeFromCart()
			throws ServletException, IOException {
		System.out.println(request.getParameter("removebook"));

		System.out.println("I removed");

		Map<String, CartBean> cartItems = (HashMap) request.getSession().getAttribute("shoppingcart");
		request.getSession().setAttribute("shoppingcart", remove(request.getParameter("removebook"), cartItems));
		request.getSession().setAttribute("carttotal",cartTotal(remove(request.getParameter("removebook"), cartItems)));
		request.getSession().setAttribute("cartsize",remove(request.getParameter("removebook"), cartItems).size());
		response.sendRedirect("/BookLand/Cart?viewcart=true");

	}
	
	
	public void updateQuantityFromCartPage()
			throws ServletException, IOException {

		int quantity = Integer.parseInt(request.getParameter("quantity"));
		String bid = request.getParameter("btnid");
		request.getSession().setAttribute("quantity", quantity);
		Map<String, CartBean> cart = (HashMap<String, CartBean>) request.getSession().getAttribute("shoppingcart");

		request.getSession().setAttribute("shoppingcart", quantityUpdate(cart, quantity, bid));
		request.getSession().setAttribute("carttotal", cartTotal(quantityUpdate(cart, quantity, bid)));
		request.getRequestDispatcher("/Cart.jspx").forward(request, response);

	}
	
	
	public Map<String, CartBean> remove(String bid, Map<String, CartBean> cart) {

		this.cart.remove(bid);
		return this.cart;

	}
	
	
	public Map<String, CartBean> quantityUpdate(Map<String, CartBean> cart, int quantity, String bid) {

		this.cart.get(bid).setQuantity(quantity);
		return this.cart;
	}

	
	public void addItemToCart()
			throws ServletException, IOException, SQLException { // checks if cart is empty if so
		// create a new ArrayList of
		// CartBean otherwise get it

		if (request.getParameter("addtocart") != null) {

			if (this.bookdB.retrieveBook(request.getParameter("addtocart")).equals("")) {

				response.sendRedirect("/BookLand/ErrorPage");
			}

			else {

				System.out.println("This is the bookId" + "and this is cart"); // from session

				String bookId = request.getParameter("addtocart");
				int price;
				int total;
				int quantity = 1;

				if (request.getSession().getAttribute("shoppingcart") != null) {

					this.cart = (HashMap) this.request.getSession().getAttribute("shoppingcart");

					try {
						BookBean book = this.bookdB.retrieveBook(bookId);

						if (book != null) {
							CartBean item = new CartBean(bookId, book.getPrice(), book.getTitle(), quantity,
									book.getUrl());

							updateCartFromHomePage(item);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					request.getRequestDispatcher("/Home").forward(request, response);
					// response.sendRedirect("/BookLand/Home");

				}

			}
		}

	}
	
	
	
	
	

}
