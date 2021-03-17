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
import bean.CartBean;
import bean.ReviewBean;

/**
 * Servlet implementation class BookStoreModel
 */
@WebServlet({ "/BookStore", "/BookStore/*", "/Login", "/Register", "/PartnerRegister" })
public class BookStore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String target;
	String login = "";
	String path;
	List<BookBean> l = null;
	List<CartBean> cart;
	String loginerror;

	int id = 0;

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
		System.out.println(request.getParameter("quantity"));
		System.out.println("The quantity is " + request.getParameter("quantity"));
		System.out.println("Remove book" + request.getParameter("removebook"));
		ServletContext context = getServletContext();
		BookStoreModel book = (BookStoreModel) context.getAttribute("BookStore");

		if (request.getParameter("registerbtn") != null) {
			System.out.println("Hi i am an ajax call");
			System.out.println("Street value is " + request.getParameter("street"));
			String fname = request.getParameter("firstName");
			String lname = request.getParameter("lastName");
			String emailAddresss = request.getParameter("email");
			String password = request.getParameter("password");
			String street = request.getParameter("street");
			String province = request.getParameter("province");
			String city = request.getParameter("city");
			String country = request.getParameter("country");
			String zip = request.getParameter("zip");
			String phone = request.getParameter("phone");

			String s = book.getEmail(emailAddresss);
			response.setContentType("application/json");

			PrintWriter out = response.getWriter();

			if (s != null && s.equals("email exists")) {

				out.printf("This email is already being used");
				out.flush();

			} else {
				try {
					book.insertIntoAddress(street, province, country, zip, phone, city);
					book.insertUserLogin(fname, lname, emailAddresss, password);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		else if (request.getServletPath().equals("/Register")) {

			this.target = "/register.jspx";

			request.getRequestDispatcher(target).forward(request, response);

		}

		else if (request.getParameter("removebook") != null) {

			List<CartBean> l = (ArrayList) request.getSession().getAttribute("shoppingcart");
			request.getSession().setAttribute("shoppingcart", book.remove(request.getParameter("removebook"), l));
			request.getSession().setAttribute("carttotal",
					book.cartTotal(book.remove(request.getParameter("removebook"), l)));
			request.getSession().setAttribute("cartsize", book.remove(request.getParameter("removebook"), l).size());
			request.getRequestDispatcher("/Cart.jspx").forward(request, response);
		}

		else if (request.getParameter("viewcart") != null) {
			System.out.println("The shopping cart is " + request.getSession().getAttribute("shoppingcart"));
			request.getRequestDispatcher("/Cart.jspx").forward(request, response);
		}

		else if (request.getParameter("quantity") != null) {
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			request.getSession().setAttribute("quantity", quantity);
			List<CartBean> l = (List<CartBean>) request.getSession().getAttribute("shoppingcart");

			// retuns a list
			request.getSession().setAttribute("shoppingcart",
					book.quantityUpdate(l, quantity, request.getParameter("btnid")));
			request.getSession().setAttribute("carttotal",
					book.cartTotal(book.quantityUpdate(l, quantity, request.getParameter("btnid"))));
			request.getRequestDispatcher("/Cart.jspx").forward(request, response);

		}

		else if (request.getParameter("loginButton") != null) { // Actual login button inside the login.jspx
			System.out.println("I pressed the login button");
			String userName = request.getParameter("Username"); // This is from login page
			String password = request.getParameter("signinpassword");
			String visitorpwd = book.getPassword(password);
			String visitorUsername = book.getEmail(userName); // Visitor login information from db
			String partnerpwd = book.getPartnerPassword(password); // Password from partner information in db
			String firstname = book.getFullName(userName);

			System.out.println("My name is" + request.getSession().getAttribute("name"));
			try { // Partner login

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

			// Visitor/Customer Login is successful
			if (visitorpwd != null && visitorUsername != null && visitorpwd.equals("password exists")
					&& visitorUsername.equals("email exists")) {

				request.removeAttribute("loginfailed");
				System.out.println("i am logged in");
				request.setAttribute("found", "false");
				request.getSession().setAttribute("name", firstname);
				try {

					l = book.retrieveBookRecords("");
					request.setAttribute("books", l);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (request.getParameter("search") == null) {

					System.out.println("On main page of book store");
					response.sendRedirect("/E-commerceoriginal/BookStore");
					request.getSession().setAttribute("hi", "hi");

				}

			}

			else { // if user login fails
				System.out.println("System not success");
				request.setAttribute("loginfailed", "failed");
				request.getRequestDispatcher("/login.jspx").forward(request, response);
				// response.sendRedirect("Login");
			}

		}

		else if (request.getServletPath().equals("/Login")) { // Going into user logins

			this.target = "/login.jspx";
			System.out.println("I am line 106");

			request.getRequestDispatcher(target).forward(request, response);

		}

		else if (request.getParameter("addtocart") != null) {
			int price;
			int total;
			int flag = 0;
//			if (!(path != null && path.equals(request.getQueryString()))) {

			path = request.getQueryString();
			System.out.println("The path is " + path);

			if (request.getSession().getAttribute("shoppingcart") == null) {
				// System.out.println("Cart is cleared");
				// book.cart.clear();
				cart = new ArrayList<CartBean>();
				request.getSession().setAttribute("shoppingcart", cart);
				request.getSession().setAttribute("carttotal", 0);
			} else {
				cart = (ArrayList) request.getSession().getAttribute("shoppingcart");
			}
			id++;
			String bid = request.getParameter("addtocart");

			try {
				String btitle = book.retrieveBookTitle(bid);
				price = book.retrievePriceofABook(bid);
				System.out.println("The price of book is " + price);

				CartBean b = new CartBean(id, bid, price, btitle, 1);
				System.out.println(b.getBookid() + " " + b.getId() + " " + b.getPrice() + " " + b.getQuantity() + " "
						+ b.getTitle());
				System.out.println("b is " + b);
				System.out.println("cart is " + cart);

				for (int i = 0; i < cart.size(); i++) {
					if (cart.get(i).getBookid().equals(bid)) {
						flag = 1;
						cart.get(i).setQuantity(cart.get(i).getQuantity() + 1);
						total = book.cartTotal(cart);
						request.getSession().setAttribute("carttotal", total);
						request.getSession().setAttribute("quantity", cart.get(i).getQuantity() + 1);
						request.getSession().setAttribute("shoppingcart", cart);
					}
				}

				if (flag != 1) {
					cart.add(b);
					System.out.println("cart items are " + cart);
					total = book.cartTotal(cart);
					request.getSession().setAttribute("cartsize", cart.size());
					request.getSession().setAttribute("carttotal", total);
					request.getSession().setAttribute("shoppingcart", cart);

					request.getSession().setAttribute("iscart", "clicked");

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// }
			try {
				l = book.retrieveBookRecords("");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("books", l);
			request.getRequestDispatcher("bookstore.jspx").forward(request, response);
		}

		else if (request.getParameter("submitreview") != null) {
			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String bid = (String) request.getSession().getAttribute("bookid");
			String review = (String) request.getParameter("writereview");
			System.out.println(review);
			try {
				book.insertAReview(fname, lname, bid, review);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (request.getParameter("reviewform") != null) {

			try {
				request.setAttribute("review", "true");
				String bid = (String) request.getSession().getAttribute("bookid");
				System.out.println("Bid is " + bid);
				request.setAttribute("bookinfo", book.retrieveInfoOfBook(bid));
				request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (request.getParameter("category") != null) {
			String category = request.getParameter("category");
			List<BookBean> books = null;
			try {
				books = book.retrieveBooksUsingCategory(category);
				request.setAttribute("category", books);
				request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		else if (request.getParameter("uidregister") != null) { // Partner signup

			int uid = Integer.parseInt(request.getParameter("uid"));
			String fname = request.getParameter("firstName");
			String lname = request.getParameter("lastName");
			String street = request.getParameter("street");
			String province = request.getParameter("province");
			String country = request.getParameter("country");
			String city = request.getParameter("city");
			String zip = request.getParameter("zip");
			String phone = request.getParameter("phone");
			
			
			
			
			
			System.out.println("uid value is " + uid);
			String partnerPassword = request.getParameter("uidpassword");
			if (book.getUID(request.getParameter("uid")) != null
					&& book.getUID(request.getParameter("uid")).equals("uid exists")) {

				System.out.println("This UID exists");

			}

			else {
				try {
					book.insertIntoAddress(street, province, country, zip, phone, city);
					book.insertPartnerLogin(uid, partnerPassword);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}

		}

		else if (request.getServletPath().equals("/PartnerRegister")) {

			this.target = "/partners.jspx";
			request.getRequestDispatcher(target).forward(request, response);

		}

	

		else if (request.getParameter("logout") != null) {
			try {
				l = book.retrieveBookRecords("");
				request.setAttribute("books", l);
				request.getSession().setAttribute("name", null);
				request.getRequestDispatcher("/bookstore.jspx").forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

		else if (request.getSession().getAttribute("userloginname") != null
				&& request.getParameter("bookinfo") != null) {
			String bid = request.getParameter("bookinfo");
			try {
				request.setAttribute("bookinfo", book.retrieveInfoOfBook(bid));
				request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (request.getParameter("bookinfo") != null) {
			System.out.println("This page");
			String bid = request.getParameter("bookinfo");
			List<ReviewBean> list = null;

			try {
				request.getSession().setAttribute("bookid", bid);
				request.setAttribute("bookinfo", book.retrieveInfoOfBook(bid));
				list = book.retrieveLastTwoReviews(bid);
				request.setAttribute("reviews", list); // This
				request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (request.getParameter("loginButton") == null) { // Login button on main bookstore
			System.out.println("The value of loginerror is " + request.getAttribute("loginfailed"));
			System.out.println("This is the first page");
			try {
				request.removeAttribute("loginfailed");
				l = book.retrieveBookRecords("");
				request.setAttribute("books", l);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.target = "/bookstore.jspx";
			request.getRequestDispatcher(target).forward(request, response);
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
