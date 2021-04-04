package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.BookDAO;
import services.CartServices;

/**
 * Servlet implementation class _Cart
 */
@WebServlet("/_Cart")
public class _Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private CartServices cart;
    private BookDAO book;
    /**
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public _Cart(HttpServletRequest request, HttpServletResponse response) throws SQLException {
    	super();
    	this.cart = new CartServices(request, response);
    	this.book =  new BookDAO();
        // TODO Auto-generated constructor stub
    }
    
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getParameter("addtocart") != null && request.getParameter("viewcart").equals("true")) {

			this.cart.displayCartPage();

		} else
			try {
				if (request.getParameter("addtocart") != null
						&& !this.book.retrieveBook(request.getParameter("addtocart")).equals("")) {

					try {
						this.cart.addItemToCart();
					} catch (ServletException | IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else if (request.getParameter("removebook") != null) {

					this.cart.removeFromCart();

				}

				else if (request.getParameter("quantity") != null) {

					this.cart.updateQuantityFromCartPage();
				}

				else {

					response.sendRedirect("ErrorPage");
				}
			} catch (SQLException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
