package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BookStoreModel;

/**
 * Servlet implementation class Analytics
 */
@WebServlet("/Analytics")
public class Analytics extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookStoreModel model;
    /**
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public Analytics() throws ClassNotFoundException {
        super();
        this.model = BookStoreModel.getInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    
	public void displayTopMonthlyBooks(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LinkedHashMap<String, LinkedHashMap<String, Integer>> result = null;
		try {
			result = this.model.retrieveBooksSoldEachMonth();
			LinkedHashMap<String, Integer> newResult;

			for (Map.Entry<String, LinkedHashMap<String, Integer>> entry : result.entrySet()) {
				newResult = entry.getValue();
				for (Map.Entry<String, Integer> entry2 : newResult.entrySet()) {

					System.out.println("This is the key" + entry2.getKey() + "this is the value" + entry2.getValue());

				}

			}

			request.setAttribute("topMonthResult", result.get(request.getParameter("topMonth")));
			request.setAttribute("chosenMonth", request.getParameter("topMonth"));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getParameter("topMonth") != null) {
			System.out.println("Im inside the top month");
			displayTopMonthlyBooks(request, response);

		}
		request.getRequestDispatcher("/analytics.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
