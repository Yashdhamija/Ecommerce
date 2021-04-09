package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BookStoreModel;

/**
 * Servlet implementation class Registration
 */
@WebServlet({ "/Register", "/PartnerRegister" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookStoreModel model;

	/**
	 * @throws SQLException 
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() throws SQLException {
		super();
		try {
			this.model = BookStoreModel.getInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}

	public void displayRegisterPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException, SQLException {

		System.out.println(request.getServletPath());

		if (request.getParameter("registerbtn") != null || request.getParameter("uidregister") != null) {

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
			
			if (this.model.isEmailTaken(email)) {
				// User exists
				request.setAttribute("emailexists", true); 
				request.getRequestDispatcher("/register.jspx").forward(request, response);
			}

			else {
				// insert new user into User DB
				this.model.insertUserLogin(fname, lname, email, password);																								// Registration
				this.model.insertIntoAddress(email,street, province, country, zipCode, phone, city);
				response.sendRedirect("/BookLand/Login?registerSuccess=true");       // made a chnage
			}

		}

		if (request.getParameter("uidregister") != null) {
			
			if (this.model.isEmailTaken(email)) { 
				// Partner exists throw error message
				request.setAttribute("partnerexists", true);
				request.getRequestDispatcher("/partners.jspx").forward(request, response);
			}

			else {
				this.model.insertPartnerLogin(email, password, fname, lname);
				this.model.insertIntoAddress(email,street, province, country, zipCode, phone, city);
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
