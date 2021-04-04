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

			request.getSession().setAttribute("reviewbookid", request.getParameter("reviewform"));
			if (request.getSession().getAttribute("name") != null) {

				try {

					request.setAttribute("review", "true");
					String bid = (String) request.getSession().getAttribute("bookid");
					System.out.println("Bid is " + bid);
					request.setAttribute("bookinfo", this.model.retrieveInfoOfBook(bid));
					request.setAttribute("reviews", this.model.retrieveLastThreeReviews(bid));
					request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {

				response.sendRedirect("/BookLand/Login");

			}

		}
		
		
//		else if(request.getServletPath() != null &&  request.getServletPath().equals("/Orders") && request.getSession().getAttribute("UserType") != null) {
//			response.sendRedirect("/BookLand/Orders");
//		}
		
		// This is triggered when the bookTitle is clicked in the bookstore.jspx
		else if (request.getParameter("bookinfo") != null
				&& !this.model.retrieveBookTitle(request.getParameter("bookinfo")).equals("")) {
			String bid = request.getParameter("bookinfo");
			request.getSession().setAttribute("bookid", bid);
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
				&& request.getSession().getAttribute("UserType").equals("partner")) { // This is for clicking the rest
																						// API for partners

			request.getRequestDispatcher("/restcall.jspx").forward(request, response);

		}

		else {
			// Error Page
			response.sendRedirect("/BookLand/ErrorPage");

		}

	}

	public void HomePage(HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, ServletException, IOException { // start the homepage of the bookstore
		BookStoreModel book = BookStoreModel.getInstance();
		List<BookBean> l;
		try {
			l = book.retrieveBookRecords("");
			request.setAttribute("books", l);
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
 
	public void BookInfoPage(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {

		if (!this.model.retrieveBookTitle(request.getParameter("bookinfo")).equals("")
				|| !this.model.retrieveBookTitle(request.getParameter("reviewform")).equals("")
				|| !this.model.retrieveBookTitle(request.getParameter("submitform")).equals("")) {

			System.out.println("Gsdsd");
			request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);

		}

	}

	public void openIndividualBook(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		// provides a new page on each book with a write a review section
		String bid = request.getParameter("bookinfo");
		List<ReviewBean> list = null;

		try {
			request.getSession().setAttribute("bookid", bid);
			request.setAttribute("bookinfo", this.model.retrieveInfoOfBook(bid));
			list = this.model.retrieveLastThreeReviews(bid);
			request.setAttribute("reviews", list); // This
			request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

			request.setAttribute("bookinfo", this.model.retrieveInfoOfBook(bid));
			request.setAttribute("reviews", this.model.retrieveLastThreeReviews(bid));
			// request.getRequestDispatcher("/bookinformation.jspx").forward(request,
			// response);
			response.sendRedirect("/BookLand/Home?bookinfo=" + bid);
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
