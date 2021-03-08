package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.BookStoreModel;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.BookBean;

/**
 * Servlet implementation class BookStoreModel
 */
@WebServlet({ "/BookStore", "/BookStore/*" })
public class BookStore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String target;
	String login = "";
	List<BookBean> l = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BookStore() {
		super();

	}

	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		ServletContext context = getServletContext();
		BookStoreModel store = new BookStoreModel();
		context.setAttribute("BookStore", store);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	

//		
		ServletContext context = getServletContext();
		BookStoreModel book = (BookStoreModel) context.getAttribute("BookStore");

		if (request.getParameter("signup") != null) {

			this.target = "/register.jspx";
			request.getRequestDispatcher(target).forward(request, response);

		}

		else if (request.getPathInfo() != null && request.getPathInfo().indexOf("Ajax") >= 0) {
			System.out.println("Hi i am an ajax call");
			String fname = request.getParameter("firstName");
			String lname = request.getParameter("lastName");
			String emailAddresss = request.getParameter("email");
			String password = request.getParameter("password");
			String s = book.getEmail(emailAddresss);
			response.setContentType("application/json");

			PrintWriter out = response.getWriter();

			if (s != null && s.equals("email exists")) {

				out.printf("This email is already being used");
				out.flush();

			} else {
				book.insertUserLogin(fname, lname, emailAddresss, password);
			}

		}

		else if (request.getParameter("partnersignup") != null) {

			this.target = "/partners.jspx";
			request.getRequestDispatcher(target).forward(request, response);

		}

		else if (request.getParameter("uidregister") != null) {

			int uid = Integer.parseInt(request.getParameter("uid"));
			String partnerPassword = request.getParameter("uidpassword");
			book.insertPartnerLogin(uid, partnerPassword);
			System.out.println("Testing purpose");

		} else if (request.getParameter("logout") != null) {
			request.getSession().setAttribute("userloginname", null);
			request.getRequestDispatcher("/login.jspx").forward(request, response);
			System.out.println("i am Logged out");
		}

		else if (request.getParameter("search") != null) {
			String search = request.getParameter("search");
			System.out.println(search);
			try {

				request.setAttribute("bookfound", book.getSearchedBook(search));
				request.setAttribute("found", "true");
				request.setAttribute("numberofresults", book.searchResultsCount(search));
				request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if(request.getSession().getAttribute("userloginname") != null && request.getParameter("bookinfo") != null) {
			String bid = request.getParameter("bookinfo");
			try {
				request.setAttribute("bookinfo", book.retrieveInfoOfBook(bid));
				request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (request.getSession().getAttribute("userloginname") != null) {
			try {

				l = book.retrieveBookRecords("");
				request.setAttribute("books", l);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
		}

		else if (request.getParameter("loginButton") == null) {

			this.target = "/login.jspx";
			request.getRequestDispatcher(target).forward(request, response);

		}

		else if (request.getParameter("loginButton") != null) {

			String userName = request.getParameter("Username"); // This is from login page
			String password = request.getParameter("signinpassword");
			String visitorpwd = book.getPassword(password);
			String visitorUsername = book.getEmail(userName); // Visitor login information from db
			String partnerpwd = book.getPartnerPassword(password); // Password from partner information in db

			try { // Partner login

				// int uid = Integer.parseInt(userName);

				if (userName.length() == 8 && partnerpwd != null && book.getUID(userName) != null
						&& partnerpwd.equals("partner password exists") && book.getUID(userName).equals("uid exists")) {

					System.out.println("Access granted for partners");
					return; // need to diffrentiate between visitor login and partner login
				}

				else {

					System.out.println("Access not granted");

				}

			}

			catch (Exception ex) {

				ex.printStackTrace();

			}

			// Visitor/Customer Login
			if (visitorpwd != null && visitorUsername != null && visitorpwd.equals("password exists")
					&& visitorUsername.equals("email exists")) {

				System.out.println("System success");
				request.getSession().setAttribute("userloginname", visitorUsername);
				request.setAttribute("found", "false");
				try {

					l = book.retrieveBookRecords("");
					request.setAttribute("books", l);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				if (request.getParameter("search") == null) {
					System.out.println("On main page of book store");
					request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
					// request.getSession().setAttribute("hi", "hi");
					// response.sendRedirect("main");

				}
				
				
				
				

			} 
			
			
			
			
			
			else {
				System.out.println("System not success");
				request.setAttribute("loginfailed", "failed");
				request.getRequestDispatcher("/login.jspx").forward(request, response);

			}

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
