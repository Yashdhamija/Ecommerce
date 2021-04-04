package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.UserBean;
import services.RegisterService;

/**
 * Servlet implementation class Registration
 */
@WebServlet({ "/Register", "/PartnerRegister" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RegisterService register;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
		super();
		try {
			this.register = new RegisterService();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
			throws SQLException, NoSuchAlgorithmException, IOException {

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
			UserBean user = this.register.isUserExist(email, password);
			
			if (user != null && user.getUserType() == 0) {
				request.setAttribute("emailexists", true); // User exists

			}

			else {

				this.register.UserRegister(fname, lname, email, password, street, city, province, zipCode, phone,
						country); // inserted new user into User DB
				response.sendRedirect("Login");
			}

		}

		if (request.getParameter("uidregister") != null) {
			UserBean user = this.register.isUserExist(email, password);
			
			if (user != null && user.getUserType() == 1) { // Partner exists throw error message				
				request.setAttribute("partnerexists", true);
			}

			else {

				this.register.PartnerRegister(fname, lname, email, password, street, city, province, zipCode, phone,
						country);
				response.sendRedirect("Login");
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
		Register register = new Register();
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
