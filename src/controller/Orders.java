package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.AddressBean;
import bean.OrderBean;
import model.BookStoreModel;

/**
 * Servlet implementation class Orders
 */
@WebServlet("/Orders")
public class Orders extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookStoreModel model;

	/**
	 * @throws ClassNotFoundException
	 * @see HttpServlet#HttpServlet()
	 */
	public Orders() throws ClassNotFoundException {
		super();
		this.model = BookStoreModel.getInstance();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {
			
			if(request.getParameter("shipping-address") != null && request.getParameter("shipping-address").equals("true")) {
				 
				request.getSession().setAttribute("showaddress", "allowed");
				
				request.getSession().setAttribute("fulladdress", this.model.retrieveAddress((String) request.getSession().getAttribute("useremail")));	
				
				request.getRequestDispatcher("/Orders.jspx").forward(request, response);
				
			}
			else if (request.getParameter("orderhistory") != null && request.getParameter("orderhistory").equals("true")
					&& request.getParameter("date") != null
					&& model.getAllUserOrderDates((String) request.getSession().getAttribute("useremail"))
							.contains(request.getParameter("date"))) {
				
				String date = request.getParameter("date");
				String email = (String) request.getSession().getAttribute("useremail");
				List<OrderBean> orderitems = model.getOrderByDate(date, email);
				
				
				request.getSession().setAttribute("orderitems", orderitems);
				request.getSession().setAttribute("d", date);
				request.getSession().setAttribute("orderview", "allowed");
				request.getRequestDispatcher("/Orders.jspx").forward(request, response);
			} else if (request.getParameter("orderhistory") != null
					&& request.getParameter("orderhistory").equals("true")) {
				request.getSession().setAttribute("orderhistory", "allowed");
				try {
					
					
					request.getSession().removeAttribute("orderview");
					request.getSession().removeAttribute("showaddress");
					request.getSession().setAttribute("dates",
							model.getAllUserOrderDates((String) request.getSession().getAttribute("useremail")));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("/Orders.jspx").forward(request, response);

			}

			else {
				
				
				if(request.getSession().getAttribute("UserType") != null) {
					request.getSession().removeAttribute("showaddress");
					request.getSession().removeAttribute("visitoraddress");
					request.getSession().removeAttribute("orderview");
					request.getSession().removeAttribute("orderhistory");
					request.getRequestDispatcher("/Orders.jspx").forward(request, response);
				}
				
				else {
					request.getRequestDispatcher("/404.jspx").forward(request, response);;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
