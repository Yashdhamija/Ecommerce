package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.CartBean;
import model.BookStoreModel;

/**
 * Servlet implementation class Admin
 */
@WebServlet({"/AdministratorLoginPage"})
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookStoreModel model;
    /**
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public Admin() throws ClassNotFoundException {
    	
        super();
        this.model = BookStoreModel.getInstance();
        // TODO Auto-generated constructor stub
    }
    
    
    
    
    public void adminLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	request.getRequestDispatcher("/Administrator.jspx").forward(request, response);
    	
    }
    
    
    public void displayAnalytics(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		LinkedHashMap<String, Integer> list;
		LinkedHashMap<String, LinkedHashMap<String, Integer>> result;
		List<List<String>> userStats;
		

		result = this.model.retrieveBooksSoldEachMonth();
	
		
		list = this.model.retrieveTopTenAllTime();
		userStats = this.model.retrieveUserStatistics();

		request.getSession().setAttribute("TopTen", list);
		request.getSession().setAttribute("topMonthList", result);
		request.getSession().setAttribute("userStats", userStats);
		//request.getRequestDispatcher("/analytics.jspx").forward(request, response);
		response.sendRedirect("/BookLand/Analytics");

    }
    


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	
		
	  if (request.getServletPath().equals("/AdministratorLoginPage") && request.getParameter("adminEmail") == null
				&& request.getParameter("adminPassword") == null) {
			adminLogin(request, response);
		}

		else if (request.getParameter("adminButton") != null) {

			String adminPassword = request.getParameter("adminPassword");
			String adminEmail = request.getParameter("adminEmail");

			try {
				if (this.model.isValidAdmin(adminEmail, adminPassword)) {
					request.getSession().setAttribute("adminValidated", "validated");
					
					displayAnalytics(request,response);
					
				}

				else {

					request.setAttribute("adminLoginFailed", "failed");
					request.getRequestDispatcher("/Administrator.jspx").forward(request, response);

				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
