package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.BookBean;
import bean.CounterBean;
import bean.ReviewBean;
import model.BookStoreModel;

/**
 * Servlet implementation class Front
 */
@WebServlet({ "/Home", "/Home/*" })
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String LOGINURL = "/BookLand/Home/Login";
	private static final String ORDERURL = "/Order";
	private static final String CARTURL = "/Cart";
	private static final String REGISTERURL = "/BookLand/Home/Register";
	private static final String PARTNERREGISTERURL = "/PartnerRegister";
	private static final String ORDERCONFIRMATION = "/OrderConfirmation";
	private static final String ADMINSTRATORLOGIN = "/AdministratorLogin";
	private BookStoreModel model;

	/**
	 * @throws ClassNotFoundException
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() throws ClassNotFoundException {
		super();
		this.model = BookStoreModel.getInstance();
		// TODO Auto-generated constructor stub
	}

	public void Dispatcher(HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, ServletException, IOException, SQLException {

		// This is for when user clicks the write a review button on the
		// bookinformation.jspx page
		if (request.getParameter("reviewform") != null && 
				!this.model.retrieveBookTitle(request.getParameter("reviewform")).equals("")) {
			 reviewForm(request, response);
		}
		// This is triggered when the bookTitle is clicked in the bookstore.jspx
		else if (request.getParameter("bookinfo") != null
				&& !this.model.retrieveBookTitle(request.getParameter("bookinfo")).equals("")) {
			openIndividualBook(request, response);

		}
		// This is called when user submits review in the bookinformation.jspx page
		else if (request.getParameter("submitreview") != null) {

			SubmitReview(request, response);
		}
		// This is all the correct URLs with \Home\*
		else if ((request.getRequestURI().equals("/BookLand/Home") && request.getQueryString() == null)
				|| (request.getRequestURI().equals("/BookLand/Home")
				&& !this.model.retrieveBookTitle(request.getParameter("addtocart")).equals(""))) {
			HomePage(request, response);
		}
		// This is for displaying the books based on category
		else if (request.getParameter("category") != null
				&& this.model.retrieveBooksUsingCategory(request.getParameter("category")).size() > 0) {

			displayBooksInCategory(request, response);

		}
		// This is for searching the books
		else if (request.getParameter("search") != null) {
			searchForBooks(request, response);
		}
		// This is for rest calls for Partners
		else if (request.getParameter("restcall") != null && request.getParameter("restcall").equals("true")
				&& request.getSession().getAttribute("UserType") != null
				&& ((String) request.getSession().getAttribute("UserType")).equals("partner")) { 
			// This is for clicking the rest API for partners
			request.getRequestDispatcher("/restcall.jspx").forward(request, response);

		}

		else {
			// Error Page
			response.sendRedirect("/BookLand/ErrorPage");
		}
	}
	
	public void reviewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getSession().setAttribute("reviewbookid", request.getParameter("reviewform"));
		
		if (request.getSession().getAttribute("name") != null) {
			request.setAttribute("review", "true");
			System.out.println("ReviewForm requested: Bid is " + (String) request.getSession().getAttribute("bookid"));
			
			// [!ref-y] saving both in sessionScope saves re-computing them here again!
//				request.setAttribute("bookinfo", this.model.retrieveInfoOfBook(bid));
//				request.setAttribute("reviews", this.model.retrieveLastThreeReviews(bid));
			request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);
		}

		else {
			response.sendRedirect("/BookLand/Login");
		}
	}

	public void HomePage(HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, ServletException, IOException { 
		// start the homepage of the bookstore
		try {
			request.setAttribute("books", this.model.retrieveBookRecords(""));
			request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayBooksInCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
			String category = request.getParameter("category");
			List<BookBean> books = null;
			try {
				books = this.model.retrieveBooksUsingCategory(category);
				request.setAttribute("category", books);
				request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	public void searchForBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String search = request.getParameter("search");
		System.out.println(search);
		try {
			// Improve this to add for more searches like category
			request.setAttribute("bookfound", this.model.getSearchedBook(search));
			request.setAttribute("found", "true");
			request.setAttribute("numberofresults", this.model.searchResultsCount(search));
			request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openIndividualBook(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		// provides a new page on each book with a write a review section
		String bid = request.getParameter("bookinfo");
		
		if (request.getSession().getAttribute("bookinfo") == null 
				|| !((BookBean) request.getSession().getAttribute("bookinfo")).getBid().equals(bid)) {
		
			try {
				request.getSession().setAttribute("bookid", bid);
				// [~ref-y] storing in session so don't re-compute when upon reloading of book information page 
				// (due to in -page action e.g, submitReview)
				request.getSession().setAttribute("bookinfo", this.model.retrieveInfoOfBook(bid));
				request.getSession().setAttribute("reviews", this.model.retrieveLastThreeReviews(bid)); // This
	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// [~ref-y] is there possibility that bookinfo is set and reviews list is not??
		}
		
		request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);
	}

	public void SubmitReview(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String bid = (String) request.getSession().getAttribute("bookid");
		String review = (String) request.getParameter("writereview");
		String title = request.getParameter("reviewtitle");

		System.out.println(review);
		try {
			this.model.insertAReview(fname, lname, bid, review, title);
			
			// [~ref-y] no need since this request is made from from bookinformation page which already contains book info
//			request.setAttribute("bookinfo", this.model.retrieveInfoOfBook(bid));
			
			// update reviews list
			request.getSession().setAttribute("reviews", this.model.retrieveLastThreeReviews(bid));
			request.getRequestDispatcher("/bookinformation.jspx").forward(request,	response);
			
			// [~ref-y] no need to redirect via HomePage again since, 
			// we got all information we need to display saved in session, no re-compuattion needed!
//			response.sendRedirect("/BookLand/Home?bookinfo=" + bid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayBooksFromCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String category = request.getParameter("category");
		List<BookBean> books = null;
		try {
			books = this.model.retrieveBooksUsingCategory(category);
			request.setAttribute("category", books);
			request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// This is for the reason where counter gets null look into this
			if (request.getSession().getAttribute("counter") == null) {
				CounterBean counter = new CounterBean();
				request.getSession().setAttribute("counter", counter);

			}

			this.Dispatcher(request, response);
			System.out.println("MY value is" + request.getParameter("loginButton"));
		} catch (ClassNotFoundException | ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
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
