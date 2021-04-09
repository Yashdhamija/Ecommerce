package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.CartBean;
import bean.ReviewBean;
import model.AdminService;

/**
 * Servlet implementation class Admin
 */
@WebServlet({"/AdministratorLoginPage"})
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminService adminService;
    /**
     * @throws ClassNotFoundException 
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public Admin() throws ClassNotFoundException, SQLException {    	
        super();
        this.adminService = AdminService.getInstance();
    }
    
    
    
    
    public void adminLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	request.getRequestDispatcher("/Administrator.jspx").forward(request, response);
    	
    }
    
    
    public void displayAnalytics(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		LinkedHashMap<String, Integer> list;
		List<List<String>> userStats;
		ArrayList<List<String>> result;
		

		
		list = this.adminService.retrieveTopTenAllTime();
		//userStats = this.model.retrieveUserStatistics();

		request.getSession().setAttribute("TopTen", list);
		//request.getSession().setAttribute("userStats", userStats);
		request.getSession().setAttribute("userStats", request.getAttribute("annomized"));
		response.sendRedirect("/BookLand/Analytics");

    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		


		
		 if ( request.getParameter("adminEmail") == null
				&& request.getParameter("adminPassword") == null && request.getQueryString() == null &&
				request.getSession().getAttribute("adminValidated") == null) {
			adminLogin(request, response);
		}
	  
	  
	  else if(request.getParameter("getReviews") != null && request.getParameter("getReviews").equals("true") && request.getSession().getAttribute("adminValidated") != null &&
				request.getSession().getAttribute("adminValidated").equals("validated")) {
		 
		  try {
			List<ReviewBean> reviews = this.adminService.retrieveAllReviews();
			
			if(reviews.size() == 0) {
				request.getSession().setAttribute("allreviews", "empty");
			}
			else {
			
			request.getSession().removeAttribute("allreviews");
			request.getSession().setAttribute("reviews", reviews);
			}
			response.sendRedirect("/BookLand/EditReviews");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  }
	  

	

		else if (request.getParameter("adminLogin") != null) {

			String adminPassword = request.getParameter("adminPassword");
			String adminEmail = request.getParameter("adminEmail");
			
			
			if( request.getSession().getAttribute("UserType") != null) {
			  	
				request.getSession().setAttribute("name", null);
				request.getSession().removeAttribute("UserType");
				// clearing the cart after logout is pressed
				request.getSession().removeAttribute("cartsize");
				request.getSession().removeAttribute("carttotal");
				request.getSession().removeAttribute("shoppingcart");
				
				
			}

			try {
				if (this.adminService.isValidAdmin(adminEmail, adminPassword)) {
					
					request.getSession().setAttribute("adminValidated", "validated");
					request.getSession().setAttribute("logged", "true");
					response.sendRedirect("/BookLand/Home");
					//displayAnalytics(request,response);
				}

				else {

					request.setAttribute("adminLoginFailed", "failed");
					request.getRequestDispatcher("/Administrator.jspx").forward(request, response);

				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	  
		else if(request.getParameter("adminReport") != null && request.getSession().getAttribute("adminValidated")!= null &&
				request.getSession().getAttribute("adminValidated").equals("validated")) {
			
			try {
				
				displayAnalytics(request,response);
			} catch (SQLException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	    
		else {
			
			 response.sendRedirect("/BookLand/Home");
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
