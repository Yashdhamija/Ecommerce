package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ReviewBean;
import model.BookStoreModel;

/**
 * Servlet implementation class EditReviews
 */
@WebServlet("/EditReviews")
public class EditReviews extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookStoreModel model;

	/**
	 * @throws ClassNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public EditReviews() throws ClassNotFoundException {
		super();
		this.model = BookStoreModel.getInstance();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		if (request.getServletPath().equals("/EditReviews") && request.getQueryString() == null && request.getSession().getAttribute("adminValidated") != null && request.getSession().getAttribute("adminValidated").equals("validated")) {
			System.out.println("The value of admin after 3 is " + request.getSession().getAttribute("adminValidated"));

			request.getRequestDispatcher("/EditAllReviews.jspx").forward(request, response);
		}
		else if(request.getParameter("removeview") != null && request.getSession().getAttribute("adminValidated") != null && request.getSession().getAttribute("adminValidated").equals("validated") ) {
			
			System.out.println("The value of admin after 4 is " + request.getSession().getAttribute("adminValidated"));

			String bid = request.getParameter("removereview");
			String review = request.getParameter("bookreview");
			
			
			List<ReviewBean> reviews;
			try {
				model.deleteAReview(bid, review);
				reviews = model.retrieveAllReviews();
				request.getSession().setAttribute("reviews", reviews);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			request.getRequestDispatcher("/EditAllReviews.jspx").forward(request, response);
			
		}
		
		else {
			response.sendRedirect("/BookLand/ErrorPage");
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