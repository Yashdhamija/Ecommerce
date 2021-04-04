package services;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.UserDAO;
import bean.UserBean;

public class _LoginService {
	
	
	
	private UserDAO userDb;
	private UserBean user; 
	private HttpServletRequest request;
	private HttpServletResponse response;
	public _LoginService(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		
		this.request = request;
		this.response = response;
		this.userDb = new UserDAO();
		
	}
	
	
	// Use Composition
	public void UserLoginValidation() throws SQLException, ServletException, IOException {
		String email = request.getParameter("Username");
		String password = request.getParameter("signinpassword");
		this.user = userDb.retrieveCustomerInfo(email,password);
		if(this.user != null) {
			
			request.getSession().setAttribute("user", this.user);
			
			
		}
		
		else {
			
			// try AJAX call
			request.setAttribute("loginfailed", "failed");
			this.displayLoginPage();
		}
		
	}
	
	
	public void displayLoginPage() throws ServletException, IOException {
		
		this.request.getRequestDispatcher("/login.jspx").forward(request, response);
		
	}
	
	
	
	
	
	
	

}
