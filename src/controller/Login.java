package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.CounterBean;
import bean.UserBean;

import javax.servlet.ServletConfig;

import model.BookStoreModel;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import services.LoginService;

/**
 * Servlet implementation class Login
 */
@WebServlet({ "/Login","/Login/*"})
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginService login;
	private BookStoreModel model;
	public CounterBean counter;

	/**
	 * @throws ClassNotFoundException
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() throws ClassNotFoundException {
		super();
		
		try {
			this.login = new LoginService();
			this.model = BookStoreModel.getInstance();
		} catch (ClassNotFoundException e) {
			// handle exception
			// throw exception and hoping it would automatically 
			// be caught and displays Eception.jspx
			e.printStackTrace();
			throw new ClassNotFoundException("Sorry, an exception occured");
		}
	}
	
	public void LoginAndLogoutFromHomPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException {
		
		// validate login credentials provided (as user or as partner)
		if (request.getParameter("loginButton") != null) {	
			LoginAuthentication(request, response);	
		} 
		
		// sign up from login page
		else if (request.getParameter("signup") != null || request.getParameter("partnersignup") != null) {
			
			response.sendRedirect("/BookLand/Register");
		}
		
		// log out from home page
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
		
		// Login From Home Page by clicking the login/register button
		else if (request.getServletPath() != null && request.getServletPath().equals("/Login")
				&& request.getQueryString() == null) { //			
			request.getRequestDispatcher("/login.jspx").forward(request, response);
		} 
		
		else {
			// error handling
		}
	}

	public void LoginAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException {

		String email = request.getParameter("Username");
		String password = request.getParameter("signinpassword");
		System.out.println(email);
		System.out.println(password);

		if (email != null && password != null) {
			UserBean user = this.model.isUserExist(email, password);
			
			if (user != null) {
				// triggers for adding to cart	
				request.getSession().setAttribute("name", user.getFirstname());
				request.getSession().setAttribute("useremail", email);
				request.getSession().setAttribute("UserType", user.getUserType() == 0 ? "visitor" : "partner"); 
				
				
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

				response.sendRedirect("/BookLand/Home");
				
			} else {
				request.setAttribute("loginfailed", "failed");
				request.getRequestDispatcher("/login.jspx").forward(request, response);
			}
		}
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			LoginAndLogoutFromHomPage(request, response);
		} catch (NoSuchAlgorithmException | ServletException | IOException e) {
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
