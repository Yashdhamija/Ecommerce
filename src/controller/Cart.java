package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.BookStoreModel;

import bean.CartBean;

/**
 * Servlet implementation class Cart
 */
@WebServlet({ "/Cart", "/Cart/*" })
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, CartBean> cart;
	private BookStoreModel model;

	/**
	 * @throws ClassNotFoundException
	 * @see HttpServlet#HttpServlet()
	 */
	public Cart() throws ClassNotFoundException {
		super();
		this.model = BookStoreModel.getInstance();
		// TODO Auto-generated constructor stub
	}

	public void displayCartPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("shoppingcart") == null) {
			
			request.getSession().setAttribute("carttotal", "0");

		}

		request.getRequestDispatcher("/Cart.jspx").forward(request, response);

	}

	public void addToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException { // checks if cart is empty if so
		// create a new ArrayList of
		// CartBean otherwise get it

		if (request.getParameter("addtocart") != null) {

			if (this.model.retrieveBookTitle(request.getParameter("addtocart")).equals("")) {

				response.sendRedirect("/BookLand/ErrorPage");
			}

			else {

		

				String bookId = request.getParameter("addtocart");
				int price;
				int total;
				int quantity = 1;

				if (request.getSession().getAttribute("shoppingcart") == null) {
				
					this.cart = new HashMap<String, CartBean>();
					request.getSession().setAttribute("shoppingcart", this.cart);
				} else {
					this.cart = (HashMap) request.getSession().getAttribute("shoppingcart");
				}

				try {
					String bookTitle = this.model.retrieveBookTitle(bookId);
					
					price = this.model.retrievePriceofABook(bookId);
					

					String imageurl = model.retrieveBookUrl(bookId);
					CartBean bookCart = new CartBean(bookId, price, bookTitle, quantity, imageurl);

					if (this.cart.containsKey(bookId)) {
						this.cart.get(bookId).setQuantity(cart.get(bookId).getQuantity() + 1);
						total = this.model.cartTotal(cart);
						request.getSession().setAttribute("carttotal", String.valueOf(total));
						request.getSession().setAttribute("quantity", cart.get(bookId).getQuantity() + 1);
						request.getSession().setAttribute("shoppingcart", cart);
					}

					else {
						
						this.cart.put(bookId, bookCart);
					
						total = this.model.cartTotal(this.cart);
						request.getSession().setAttribute("cartsize", cart.size());
						request.getSession().setAttribute("carttotal", String.valueOf(total));
						request.getSession().setAttribute("shoppingcart", cart);
						request.getSession().setAttribute("iscart", "clicked");
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

	public void removeFromCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		Map<String, CartBean> cartItems = (HashMap) request.getSession().getAttribute("shoppingcart");
		request.getSession().setAttribute("shoppingcart",
				this.model.remove(request.getParameter("removebook"), cartItems));
		request.getSession().setAttribute("carttotal",
				this.model.cartTotal(this.model.remove(request.getParameter("removebook"), cartItems)));
		request.getSession().setAttribute("cartsize",
				this.model.remove(request.getParameter("removebook"), cartItems).size());
		response.sendRedirect("/BookLand/Cart?viewcart=true");

	}

	public void updateQuantityInCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int quantity = Integer.parseInt(request.getParameter("quantity"));
		String bid = request.getParameter("btnid");
		request.getSession().setAttribute("quantity", quantity);
		Map<String,CartBean> cart = (HashMap<String,CartBean>) request.getSession().getAttribute("shoppingcart");

		
		request.getSession().setAttribute("shoppingcart",this.model.quantityUpdate(cart, quantity, bid));
		request.getSession().setAttribute("carttotal",
				this.model.cartTotal(this.model.quantityUpdate(cart, quantity, bid)));
		request.getRequestDispatcher("/Cart.jspx").forward(request, response);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		try {
			if (request.getParameter("viewcart") != null && request.getParameter("viewcart").equals("true")) {
				displayCartPage(request, response);
			} 
			
			else if (request.getParameter("addtocart") != null 
					&& !this.model.retrieveBookTitle(request.getParameter("addtocart")).equals("")) {
					addToCart(request, response);
			}
			
			else if (request.getParameter("removebook") != null) {
				removeFromCart(request, response);
			}
			
			else if (request.getParameter("quantity") != null) {
				
				updateQuantityInCart(request,response);
			}

			else {

				response.sendRedirect("ErrorPage");
			}
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
