package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.UserBean;
import bean.CartBean;
import bean.CounterBean;
import model.BookStoreModel;

/**
 * Servlet implementation class Payment
 */
@WebServlet("/Payment")
public class Payment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookStoreModel model;

	/**
	 * @throws ClassNotFoundException
	 * @see HttpServlet#HttpServlet()
	 */
	public Payment() throws ClassNotFoundException {
		super();
		this.model = BookStoreModel.getInstance();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @throws SQLException
	 * @throws IOException 
	 * @throws ServletException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	public void retrieveUserInformation(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {

		if (request.getSession().getAttribute("UserType").equals("visitor")) {

			request.getSession().setAttribute("fulladdress",
					this.model.retrieveAddress((String) request.getSession().getAttribute("useremail")));
			request.getSession().setAttribute("userinfo",
					this.model.retrieveUserInfo((String) request.getSession().getAttribute("useremail")));

		}
		// Partner
		else {

			request.getSession().setAttribute("fulladdress",
					this.model.retrieveAddress((String) request.getSession().getAttribute("useremail")));

			request.getSession().setAttribute("userinfo",
					this.model.retrieveUserInfo((String) request.getSession().getAttribute("useremail")));
		}

		request.getRequestDispatcher("/payment.jspx").forward(request, response);

	}
	
	public void paymentSuccessful(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, CartBean> cart = (HashMap<String, CartBean>) request.getSession().getAttribute("shoppingcart");

		try {// Everything inside the try is used to insert into PO, this is successful case

			int orderId = this.model.OrderNumberGenerator();

			this.model.insertPO(orderId, ((UserBean) request.getSession().getAttribute("userinfo")).getFirstname(),
					((UserBean) request.getSession().getAttribute("userinfo")).getLastname(), "ORDERED",
					(String) request.getSession().getAttribute("useremail"));

			for (Map.Entry<String, CartBean> entry : cart.entrySet()) {

				
				this.model.insertPOItem(orderId, entry.getValue().getBookid(), entry.getValue().getPrice(),
						entry.getValue().getQuantity());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getSession().removeAttribute("shoppingcart");
		request.getSession().removeAttribute("carttotal");
		request.getSession().removeAttribute("cartsize");
		CounterBean counter = (CounterBean) request.getSession().getAttribute("counter");

		request.getSession().setAttribute("counter", counter);
;
		response.sendRedirect("/BookLand/OrderConfirmation");

	}

	public void paymentDeclined(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Map<String, CartBean> cart = (HashMap<String, CartBean>) request.getSession().getAttribute("shoppingcart");

		try {// Everything inside the try is used to insert into PO, this is declined case
			this.model.insertPO(this.model.OrderNumberGenerator(),
					((UserBean) request.getSession().getAttribute("userinfo")).getFirstname(),
					((UserBean) request.getSession().getAttribute("userinfo")).getLastname(), "DENIED",
					(String) request.getSession().getAttribute("useremail"));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CounterBean counter =  (CounterBean) request.getSession().getAttribute("counter");
		
		counter.resetCounter(); 
		request.getSession().setAttribute("counter", counter);
		request.setAttribute("authorization", "failed");
		request.getRequestDispatcher("/payment.jspx").forward(request, response);


	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (request.getServletPath() != null && request.getServletPath().equals("/Payment") &&
		 request.getSession().getAttribute("cartsize") != null && (Integer) 
		 request.getSession().getAttribute("cartsize") == 0) {
			response.sendRedirect("/BookLand/Cart?viewcart=true");
		}

		else if (request.getServletPath() != null && request.getServletPath().equals("/Payment")
				&& request.getSession().getAttribute("cartsize") != null &&
				(Integer) request.getSession().getAttribute("cartsize") != 0 && 
				request.getParameter("orderconfirmed") == null ) {
			
			
		 if(request.getSession().getAttribute("adminValidated") != null) {
			 
			 	request.getSession().removeAttribute("adminValidated");
			 	request.getSession().removeAttribute("cartsize");
			 	request.getSession().removeAttribute("carttotal");
				request.getSession().removeAttribute("shoppingcart");
				
			
			
		}
			
		  if (request.getSession().getAttribute("UserType") != null) {
				try {
					retrieveUserInformation(request, response);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			else {
				
				response.sendRedirect("/BookLand/Login");
			}
		}
		
					
			else if (request.getServletPath() != null && request.getServletPath().equals("/Payment")
					&& request.getSession().getAttribute("cartsize") != null 
					&& (Integer) request.getSession().getAttribute("cartsize") != 0
					&& request.getParameter("orderconfirmed") != null) {
				
					System.out.println("FF");
					
					
					CounterBean counter = (CounterBean) request.getSession().getAttribute("counter");
					if(counter != null) {		
						counter.updateCounter();
						
					}
					
				if (counter != null
						&&  counter.getCounter() < 3) {
					
					
					paymentSuccessful(request, response);			 
				}

				else {
					
					paymentDeclined(request, response);
					//CounterBean counter  = ((CounterBean) request.getSession().getAttribute("counter"));
				}

			}

			else {// Go Back to Login Page
				
				
				response.sendRedirect("/BookLand/Login");

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
