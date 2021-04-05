package services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.DAO;
import model.BookStoreModel;

public class LoginService {

	
    private BookStoreModel book;
	public LoginService() throws ClassNotFoundException {

		this.book = BookStoreModel.getInstance();

	}
	
	public void displayLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // Initially displays login page
		
		request.getRequestDispatcher("/login.jspx").forward(request, response);
	}
	
	
	public String displayUserLoginName(String email) {
		
		return this.book.getCustomerName(email);
		
	}
	
	public String displayPartnerName(String email) {
		
		return this.book.getPartnerName(email);
	}
	
	
	
	 

	public void UserRegister(String fname, String lname, String email, String password, String address, String city,
			String province, String zipCode, String phoneNumber, String country) throws SQLException { // User Registration
		
		this.book.insertUserLogin(fname, lname, email, password);
		this.book.insertIntoAddress(email,address, province, country, zipCode, phoneNumber, city);
	}
	
	public void  PartnerRegister(String fname, String lname, String email, String password, String address, String city,
			String province, String zipCode, String phoneNumber, String country) throws SQLException { // Partner
		
		this.book.insertUserLogin(fname, lname, email, password);
		this.book.insertIntoAddress(email,address, province, country, zipCode, phoneNumber, city);
	}
	
	
	
	
	
	public void editUser(String password,String fname, String lname, String email,String address, String city,
			String province, String zipCode, String phoneNumber, String country) {
		
		// Create db method to edit address and customer db
		
		
	} 

}
