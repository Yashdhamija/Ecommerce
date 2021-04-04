package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.CounterBean;

import javax.servlet.ServletConfig;

import model.BookStoreModel;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import services.LoginService;

/**
 * Servlet implementation class Login
 */
@WebServlet({ "/Login"})
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginService login;
	public CounterBean counter;

	/**
	 * @throws ClassNotFoundException
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() throws ClassNotFoundException {

		super();
		this.login = new LoginService();
		//this.counter= new CounterBean();
		
	
		

		// TODO Auto-generated constructor stub
	}
	
	



	public void LoginAndLogoutFromHomPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException {
		// Login From Home Page by clicking the login/register button
		
		
		if (request.getParameter("loginButton") != null) {
			
			LoginAuthentication(request, response);
		}
		else if(request.getParameter("logout") != null) {
			
			request.getSession().setAttribute("name", null);
			request.getSession().removeAttribute("UserType");
			// clearing the cart after logout is pressed
			request.getSession().removeAttribute("cartsize");
			request.getSession().removeAttribute("carttotal");
			request.getSession().removeAttribute("shoppingcart");
			request.getSession().removeAttribute("adminValidated");
			response.sendRedirect("/BookLand/Home");
		}
		
		else if (request.getServletPath() != null && request.getServletPath().equals("/Login")
				&& request.getQueryString() == null) { //
			
			this.login.displayLoginPage(request, response);
		} 
		
		else {

			// error handling
		}

	}

	public void LoginAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException {

		boolean isVisitorAuthenticated = false;
		boolean isPartnerAuthenticated = false;
		String email = request.getParameter("Username");
		String password = request.getParameter("signinpassword");
		System.out.println(email);
		System.out.println(password);

		if (email != null && password != null) {

			isVisitorAuthenticated = this.login.UserLoginValidation(email, password);
			isPartnerAuthenticated = this.login.PartnerLoginValidation(email, password);

			if (isVisitorAuthenticated || isPartnerAuthenticated) {

				if (isVisitorAuthenticated) {
					String userLoginName = this.login.displayUserLoginName(email);
					request.getSession().setAttribute("name", userLoginName); // trigger for adding to cart
					request.getSession().setAttribute("UserType", "visitor"); // trigger for adding to cart
				}

				else {
					String partnerLoginName = this.login.displayPartnerName(email);
					request.getSession().setAttribute("name", partnerLoginName);
					request.getSession().setAttribute("UserType", "partner");

				}
				
				request.getSession().setAttribute("useremail", email);
				
				if (request.getSession().getAttribute("counter") == null) {
					System.out.println("I am in 1");
					this.counter = new CounterBean();
					request.getSession().setAttribute("counter", this.counter);
				} else {
					System.out.println("I am in 2");
					this.counter = (CounterBean) request.getSession().getAttribute("counter");
					request.getSession().setAttribute("counter", this.counter);
				}
				
				//request.getSession().setAttribute("counter", this.counter); // payment requests is initiated as counterbean

				//request.getRequestDispatcher("/Home").forward(request, response);
				response.sendRedirect("/BookLand/Home");
			} else {
				request.setAttribute("loginfailed", "failed");
				request.getRequestDispatcher("/login.jspx").forward(request, response);
				; // display error message

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

		try {
			LoginAndLogoutFromHomPage(request, response);
		} catch (NoSuchAlgorithmException | ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Logout(request, response);

		// response.getWriter().append("Served at: ").append(request.getContextPath());
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
