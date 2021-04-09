package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AdminService;


/**
 * Servlet implementation class Analytics
 */
@WebServlet("/Analytics")
public class Analytics extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminService adminService;
    /**
     * @throws ClassNotFoundException 
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public Analytics() throws ClassNotFoundException, SQLException {
        super();
        this.adminService = AdminService.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    
	public void displayTopMonthlyBooks(HttpServletRequest request, HttpServletResponse response, String date)
			throws ServletException, IOException {
		//LinkedHashMap<String, LinkedHashMap<String, Integer>> result = null;
		ArrayList<List<String>> result;
		
		try {
			//result = this.model.retrieveBooksSoldEachMonth();
			//LinkedHashMap<String, Integer> newResult;

			result = this.adminService.retrieveBooksSoldEachMonth(date);
			
			
			request.setAttribute("topMonthResult", result);
			request.getSession().setAttribute("chosenMonth", request.getParameter("topMonth"));
			
		
			
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if(request.getSession().getAttribute("adminValidated") != null && request.getSession().getAttribute("adminValidated").equals("validated")) {
			try {
				request.setAttribute("date", this.adminService.getAllDates());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (request.getParameter("topMonth") != null ) {
				String date = request.getParameter("topMonth");
				displayTopMonthlyBooks(request, response, date);
	
			}
		request.getRequestDispatcher("/analytics.jspx").forward(request, response);
		
		}
		
		else {
			response.sendRedirect("/BookLand/ErrorPage");
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
