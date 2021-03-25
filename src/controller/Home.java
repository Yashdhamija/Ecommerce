package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
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
import bean.CounterBean;
import bean.ReviewBean;
import bean.UserBean;

/**
 * Servlet implementation class BookStoreModel
 */
@WebServlet({ "/Home", "/Login", "/Register", "/PartnerRegister", "/Payment", "/OrderConfirmation" }) 

public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String target;
	String login = "";
	String path;
	List<BookBean> l = null;
	List<CartBean> cart;
	CounterBean counter;
	String loginerror;
	

	int id = 0;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() {
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
			String fname = request.getParameter("firstName");
			String lname = request.getParameter("lastName");
			String emailAddress = request.getParameter("email");
			String street = request.getParameter("street");
			String province = request.getParameter("province");
			String city = request.getParameter("city");
			String country = request.getParameter("country");
			String zip = request.getParameter("zip");
			String phone = request.getParameter("phone");

			String userEmail = book.getEmail(emailAddress);
			String partnerEmail = book.getPartnerEmail(emailAddress);
			response.setContentType("application/json");

			PrintWriter out = response.getWriter();

			if (userEmail != null && userEmail.equals("email exists")
					|| partnerEmail != null && partnerEmail.equals("partner email exists")) {
				request.setAttribute("emailexists", "yes");
				request.getRequestDispatcher("/register.jspx").forward(request, response);

			}

			else {
				try {
					String password = book.encryptPassword(request.getParameter("password"));
					book.insertIntoAddress(street, province, country, zip, phone, city);
					book.insertUserLogin(fname, lname, emailAddress, password);
					// request.getRequestDispatcher("/login.jspx").forward(request, response);
					response.sendRedirect("Login");

				} catch (SQLException | NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		else if (request.getServletPath().equals("/OrderConfirmation")) {

			// book.incrementPaymentCounter(); This was here original

			counter = (CounterBean) request.getSession().getAttribute("counter");

			if (counter != null && counter.getCounter() == 3) {

				counter.resetCounter();
				System.out.println("The counter has been successffuly reset " + counter.getCounter());
			}

			if (counter != null) {
				counter.updateCounter();
				request.getSession().setAttribute("countervalue", counter.getCounter());
				System.out.println("This is the value of the consecutive calls" + counter.getCounter());
			}

			if (counter != null && counter.getCounter() <= 2) {

				int total = 0;

				try {
					book.insertPO(book.OrderNumberGenerator(),
							((UserBean) request.getSession().getAttribute("userbean")).getFname(),
							((UserBean) request.getSession().getAttribute("userbean")).getLname(), "ORDERED",
							(String) request.getSession().getAttribute("visitoremail"));

					for (int i = 0; i < cart.size(); i++) {

						book.insertPOItem((String) request.getSession().getAttribute("visitoremail"),
								cart.get(i).getBookid(), cart.get(i).getPrice(), cart.get(i).getQuantity());

						System.out.println("dfdafafdadf " + cart.get(i).getBookid() + " " + cart.get(i).getPrice() + " "
								+ cart.get(i).getQuantity() + "\n"
								+ (String) request.getSession().getAttribute("visitoremail"));

					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("/Confirmation.jspx").forward(request, response);
				request.getSession().removeAttribute("shoppingcart");
				request.getSession().removeAttribute("carttotal");
				request.getSession().removeAttribute("cartsize");

			}

			else {

				request.setAttribute("authorization", "failed");
				request.getRequestDispatcher("/payment.jspx").forward(request, response);

			}

		}

		else if (request.getServletPath().equals("/Payment")
				&& (Integer) request.getSession().getAttribute("cartsize") == 0) {
			response.sendRedirect("Home?viewcart=true");

		}

		else if (request.getServletPath().equals("/Payment")
				&& (Integer) request.getSession().getAttribute("cartsize") != 0) {

			System.out.println("dsdfasdfaf" + request.getSession().getAttribute("cartsize"));

			if (request.getSession().getAttribute("name") != null) {
				try {

					if (request.getSession().getAttribute("UserType") != null
							&& request.getSession().getAttribute("UserType").equals("visitor")) {

						request.getSession().setAttribute("fulladdress",
								book.retrieveAddress((String) request.getSession().getAttribute("visitoremail")));
						request.getSession().setAttribute("userinfo",
								book.retrieveUserInfo((String) request.getSession().getAttribute("visitoremail")));

					}

					else {

						request.getSession().setAttribute("fulladdress",
								book.retrieveAddress((String) request.getSession().getAttribute("visitoremail")));

						request.getSession().setAttribute("userinfo",
								book.retrievePartnerInfo((String) request.getSession().getAttribute("visitoremail")));

					}
					request.getRequestDispatcher("/payment.jspx").forward(request, response);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				response.sendRedirect("Login");
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

		else if (request.getParameter("addtocart") != null) {
			int price;
			int total;
			int flag = 0;

			if (request.getSession().getAttribute("shoppingcart") == null) {
				// System.out.println("Cart is cleared");
				// book.cart.clear();
				System.out.println("Cart is empty");
				cart = new ArrayList<CartBean>();
				request.getSession().setAttribute("shoppingcart", cart);

			} else {
				cart = (ArrayList) request.getSession().getAttribute("shoppingcart");
			}
			id++;
			String bid = request.getParameter("addtocart");

			try {
				String btitle = book.retrieveBookTitle(bid);
				price = book.retrievePriceofABook(bid);

				CartBean b = new CartBean(id, bid, price, btitle, 1);

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

		else if (request.getParameter("viewcart") != null) {
			System.out.println("The shopping cart is " + request.getSession().getAttribute("shoppingcart"));

			if (request.getSession().getAttribute("shoppingcart") == null) {
				request.getSession().setAttribute("carttotal", "0");
			}

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
			String email = request.getParameter("Username"); // This is from login page
			String password = request.getParameter("signinpassword");

			String visitorUsername = book.getEmail(email); // Visitor login information from db
			String loginName;

			boolean partnerAuthenticated = false;
			String partnerpwd = null;
			try {
				partnerpwd = book.getPartnerPassword(book.encryptPassword(password));
			} catch (NoSuchAlgorithmException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			String visitorpwd = null;
			try {
				visitorpwd = book.getPassword(book.encryptPassword(password));
			} catch (NoSuchAlgorithmException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// Visitor/Customer Login is successful
			if ((visitorpwd != null && visitorUsername != null && visitorpwd.equals("password exists")
					&& visitorUsername.equals("email exists"))
					|| (partnerpwd != null && book.getPartnerEmail(email) != null
							&& partnerpwd.equals("partner password exists")
							&& book.getPartnerEmail(email).equals("partner email exists"))) {

				if (visitorUsername != null && visitorpwd != null && visitorUsername.equals("email exists")
						&& visitorpwd.equals("password exists")) {
					loginName = book.getCustomerName(email);
					request.getSession().setAttribute("UserType", "visitor");
					System.out.println("This is the login name" + loginName);
				} else {

					loginName = book.getPartnerName(email);
					System.out.println("This is the login name" + loginName);
					request.getSession().setAttribute("UserType", "partner");
				}

				if (request.getSession().getAttribute("counter") == null) {
					System.out.println("I am in 1");
					counter = new CounterBean();
					request.getSession().setAttribute("counter", counter);
				} else {
					System.out.println("I am in 2");
					counter = (CounterBean) request.getSession().getAttribute("counter");
					request.getSession().setAttribute("counter", counter);
				}
				
				System.out.println("The email address is " + email);
				request.getSession().setAttribute("visitoremail", email); // saving email in the login page in
																			// session

				try {
					if (book.retrieveUserInfo(email) != null) {

						request.getSession().setAttribute("userbean", book.retrieveUserInfo(email));
						System.out.println("Helloooo" + book.retrieveUserInfo(email));

					}

					else {

						request.getSession().setAttribute("userbean", book.retrievePartnerInfo(email));
					}

				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				request.removeAttribute("loginfailed");
				try {
					System.out.println("The encrypted password is " + book.encryptPassword(password));
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("i am logged in");
				request.setAttribute("found", "false");
				request.getSession().setAttribute("name", loginName);
				try {

					l = book.retrieveBookRecords("");
					request.setAttribute("books", l);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (request.getParameter("search") == null) {

					response.sendRedirect("/BookLand/Home");
					request.getSession().setAttribute("hi", "hi");

				}

			}

			else { // if visitor or partner login fails
				System.out.println("System not success");
				request.setAttribute("loginfailed", "failed");
				request.getRequestDispatcher("/login.jspx").forward(request, response);

			}

		}

		else if (request.getServletPath().equals("/Login")) { // Going into user logins
			this.target = "/login.jspx";
			System.out.println("I am line 106");

			request.getRequestDispatcher(target).forward(request, response);

		}

		else if (request.getParameter("submitreview") != null) {

			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String bid = (String) request.getSession().getAttribute("bookid");
			String review = (String) request.getParameter("writereview");
			String title = request.getParameter("reviewtitle");

			System.out.println(review);
			try {
				book.insertAReview(fname, lname, bid, review, title);

				request.setAttribute("bookinfo", book.retrieveInfoOfBook(bid));
				request.setAttribute("reviews", book.retrieveLastThreeReviews(bid));
				request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		else if (request.getParameter("reviewform") != null) {
			request.getSession().setAttribute("reviewbookid", request.getParameter("reviewform"));
			if (request.getSession().getAttribute("name") != null) {

				try {

					request.setAttribute("review", "true");
					String bid = (String) request.getSession().getAttribute("bookid");
					System.out.println("Bid is " + bid);
					request.setAttribute("bookinfo", book.retrieveInfoOfBook(bid));
					request.setAttribute("reviews", book.retrieveLastThreeReviews(bid));
					request.getRequestDispatcher("/bookinformation.jspx").forward(request, response);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {

				response.sendRedirect("Login");

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

			String email = request.getParameter("email");
			String fname = request.getParameter("firstName");
			String lname = request.getParameter("lastName");
			String street = request.getParameter("street");
			String province = request.getParameter("province");
			String country = request.getParameter("country");
			String city = request.getParameter("city");
			String zip = request.getParameter("zip");
			String phone = request.getParameter("phone");

			String userEmail = book.getEmail(email);

			System.out.println("email value is " + email);

			if (book.getPartnerEmail(request.getParameter("email")) != null
					&& book.getPartnerEmail(request.getParameter("email")).equals("partner email exists")
					|| (userEmail != null && userEmail.equals("email exists"))) {

				System.out.println("This email exists");
				request.setAttribute("uidexists", "yes");
				request.getRequestDispatcher("/partners.jspx").forward(request, response);

			}

			else {
				try {
					// partner password is encrypted and inserted to dB
					String partnerPassword = book.encryptPassword(request.getParameter("uidpassword"));
					book.insertIntoAddress(street, province, country, zip, phone, city);
					book.insertPartnerLogin(email, partnerPassword, fname, lname);
					// request.getRequestDispatcher("/login.jspx").forward(request, response);
					response.sendRedirect("Login");

				} catch (SQLException | NoSuchAlgorithmException e) {
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
				request.getSession().removeAttribute("shoppingcart");      // Added this so if a user logs out cart is cleared
				request.getSession().removeAttribute("carttotal");
				request.getSession().removeAttribute("cartsize");
				
				
				l = book.retrieveBookRecords("");
				request.setAttribute("books", l);
				request.getSession().setAttribute("name", null);
				// book.resetPaymentCounter();

				counter = (CounterBean) request.getSession().getAttribute("counter");
				if (counter != null) {
					counter.resetCounter();
					System.out.println(
							"The valueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee of counter after logout is "
									+ counter.getCounter());
				}

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
				list = book.retrieveLastThreeReviews(bid);
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
