package services;

import java.sql.SQLException;

import model.BookStoreModel;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import DAO.DAO;
import bean.UserBean;

public class RegisterService {

	private BookStoreModel book;

	public RegisterService() throws ClassNotFoundException {

		this.book = BookStoreModel.getInstance();

	}

	public void UserRegister(String fname, String lname, String email, String password, String address, String city,
			String province, String zipCode, String phoneNumber, String country) throws SQLException, NoSuchAlgorithmException { // User
		
		this.book.insertUserLogin(fname, lname, email, this.book.encryptPassword(password));																								// Registration
		this.book.insertIntoAddress(email,address, province, country, zipCode, phoneNumber, city);				
	}
	
	public UserBean isUserExist(String email, String password) throws NoSuchAlgorithmException {
		return this.book.isUserExist(email, password);
	}
	
	public void PartnerRegister(String fname, String lname, String email, String password, String address, String city,
			String province, String zipCode, String phoneNumber, String country) throws SQLException, NoSuchAlgorithmException { // User
																										// Registration
		
		this.book.insertPartnerLogin(email, this.book.encryptPassword(password), fname, lname);
		this.book.insertIntoAddress(email,address, province, country, zipCode, phoneNumber, city);
	
	}

}
