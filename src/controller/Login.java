package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bean.UserBean;
import model.UserService;

/**
 * Servlet implementation class Login
 */
@WebServlet({ "/Login","/Login/*"})
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;

	/**
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() throws ClassNotFoundException, SQLException {
		super();
		this.userService = UserService.getInstance();
	}

	public void LoginAndLogoutFromHomPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException {
		
		
		// Login From Home Page by clicking the login/register button		
		if (request.getParameter("loginButton") != null) {
			
			LoginAuthentication(request, response);
		}
		
		// sign up from login page
		else if (request.getParameter("signup") != null) {
			
			response.sendRedirect("/BookLand/Register");
		}
		
		else if (request.getParameter("partnersignup") != null) {
			
			response.sendRedirect("/BookLand/PartnerRegister");
		}
		
		else if(request.getParameter("logout") != null && request.getSession().getAttribute("UserType") != null) {
			
			request.getSession().setAttribute("name", null);
			request.getSession().removeAttribute("UserType");
			if (request.getSession().getAttribute("partnerKey") != null) {
				request.getSession().removeAttribute("partnerKey");
			}
			// clearing the cart after logout is pressed
			request.getSession().removeAttribute("cartsize");
			request.getSession().removeAttribute("carttotal");
			request.getSession().removeAttribute("shoppingcart");
			
			response.sendRedirect("/BookLand/Home");
		}
		
		
		else if(request.getParameter("logout") != null && request.getSession().getAttribute("adminValidated") != null) {
			
			request.getSession().removeAttribute("adminValidated");
			request.getSession().removeAttribute("cartsize");
			request.getSession().removeAttribute("carttotal");
			request.getSession().removeAttribute("shoppingcart");
			response.sendRedirect("/BookLand/Home");
		}
		
		else if(request.getServletPath() != null && request.getQueryString() !=null && request.getQueryString().equals("registerSuccess=true")) {
			request.getRequestDispatcher("/login.jspx").forward(request, response);
			
		}
		
		else if(request.getServletPath() != null && request.getServletPath().equals("/Login")
				&& request.getQueryString() == null && request.getSession().getAttribute("UserType") != null) {
			
			
			response.sendRedirect("/BookLand/Home");
			
		}
		
		else if (request.getServletPath() != null && request.getServletPath().equals("/Login")
				&& request.getQueryString() == null) { //
			
			request.getRequestDispatcher("/login.jspx").forward(request, response);
		} 
		
		else {
			// error handling
			response.sendRedirect("/BookLand/Login");
		}

	}

	public void LoginAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException {

		String email = request.getParameter("Username");
		String password = request.getParameter("signinpassword");


		if (email != null && password != null) {
			UserBean user = this.userService.isUserExist(email, password);
			if (user != null) {
				// triggers for adding to cart	
				request.getSession().setAttribute("name", user.getFirstname());
				request.getSession().setAttribute("useremail", email);
				request.getSession().setAttribute("UserType", user.getUserType() == 0 ? "visitor" : "partner");
				
				if (user.getUserType() == 1) {
					request.getSession().setAttribute("partnerKey", this.userService.getpartnerKey(user.getEmail()));
				}
				
				// remove adminValidated if signed in
				if(request.getSession().getAttribute("adminValidated") != null) {
					
					request.getSession().removeAttribute("adminValidated");
				}
				
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
