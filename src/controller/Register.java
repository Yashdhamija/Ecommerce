package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.UserService;

/**
 * Servlet implementation class Registration
 */
@WebServlet({ "/Register", "/PartnerRegister" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;

	/**
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() throws SQLException, ClassNotFoundException {
		super();
		this.userService = UserService.getInstance();
	}

	public void displayRegisterPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException, SQLException {

		if(request.getServletPath() != null && (request.getServletPath().equals("/Register") || request.getServletPath().equals("/PartnerRegister"))
					&& request.getQueryString() == null && request.getSession().getAttribute("UserType") != null) {
				
				response.sendRedirect("/BookLand/Home");
				
			}

		else if (request.getParameter("registerbtn") != null || request.getParameter("uidregister") != null) {

			insertUserRegistration(request, response);

		}

		else if (request.getServletPath() != null && request.getServletPath().equals("/Register")) {

			request.getRequestDispatcher("/register.jspx").forward(request, response);
		}

		else if (request.getServletPath() != null && request.getServletPath().equals("/PartnerRegister")) {

			request.getRequestDispatcher("/partners.jspx").forward(request, response);
		}

		else {
			
			// Error handling
		}

	}

	public void insertUserRegistration(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, NoSuchAlgorithmException, IOException, ServletException {

		String fname = request.getParameter("firstName");
		String lname = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		String street = request.getParameter("street");
		String city = request.getParameter("city");
		String province = request.getParameter("province");
		String zipCode = request.getParameter("zip");
		String phone = request.getParameter("phone");
		String country = request.getParameter("country");

		if (request.getParameter("registerbtn") != null) {
			
			if (this.userService.isEmailTaken(email)) {
				// User exists
				request.setAttribute("emailexists", true); 
				request.getRequestDispatcher("/register.jspx").forward(request, response);
			}

			else {
				// insert new user into User DB
				this.userService.insertUserLogin(fname, lname, email, password);																								
				// Registration
				this.userService.insertIntoAddress(email,street, province, country, zipCode, phone, city);
				response.sendRedirect("/BookLand/Login?registerSuccess=true");
			}

		}

		if (request.getParameter("uidregister") != null) {
			
			if (this.userService.isEmailTaken(email)) { 
				// Partner exists throw error message
				request.setAttribute("partnerexists", true);
				request.getRequestDispatcher("/partners.jspx").forward(request, response);
			}

			else {
				this.userService.insertPartnerLogin(email, password, fname, lname);
				this.userService.insertIntoAddress(email,street, province, country, zipCode, phone, city);
                response.sendRedirect("/BookLand/Login?registerSuccess=true");
			}
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Register register = null;
		try {
			register = new Register();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			register.displayRegisterPage(request, response);
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		catch (NoSuchAlgorithmException e) {
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
