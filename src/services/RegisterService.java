package services;

import java.sql.SQLException;

import model.BookStoreModel;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import DAO.DAO;

public class RegisterService {

	private BookStoreModel book;

	public RegisterService() throws ClassNotFoundException {

		this.book = BookStoreModel.getInstance();

	}

	public void UserRegister(String fname, String lname, String email, String password, String address, String city,
			String province, String zipCode, String phoneNumber, String country) throws SQLException, NoSuchAlgorithmException { // User
		
		this.book.insertUserLogin(fname, lname, email, this.book.encryptPassword(password));																								// Registration
		this.book.insertIntoAddress(address, province, country, zipCode, phoneNumber, city);
		
		
	}
	
	
	public void PartnerRegister(String fname, String lname, String email, String password, String address, String city,
			String province, String zipCode, String phoneNumber, String country) throws SQLException, NoSuchAlgorithmException { // User
																										// Registration
		
		this.book.insertPartnerLogin(email, this.book.encryptPassword(password), fname, lname);
		this.book.insertIntoAddress(address, province, country, zipCode, phoneNumber, city);
	
	}
	
	
	
	
	
	public boolean isVisitorRegistererInfoInDB(String email, String password) throws NoSuchAlgorithmException {
		
		return this.book.isVisitorExist(email, this.book.encryptPassword(password));
		
	}
	
	
	public boolean isPartnerRegistererInfoInDB(String email,String password) throws NoSuchAlgorithmException {
		
		return this.book.isPartnerExist(email, this.book.encryptPassword(password));
	}

}
