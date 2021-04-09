package model;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import DAO.AddressDAO;
import DAO.UserDAO;
import bean.AddressBean;
import bean.BookBean;
import bean.OrderBean;
import bean.UserBean;
import security.PasswordEncryption;

public class UserService {

	private static UserService instance;
	private AddressDAO address;
	private UserDAO user;

	public UserService() throws SQLException {
		this.address = new AddressDAO();
		this.user =  new UserDAO();
	}
	
	public static UserService getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new UserService();
		}
		return instance;
	}
	
	public int insertUserLogin(String fname, String lname, String email, String password) throws SQLException, NoSuchAlgorithmException {

        return this.user.insertUserDB(fname, lname, email, PasswordEncryption.encryptPassword(password));
	}

	public void insertPartnerLogin(String email, String password, String fname, String lname) throws SQLException, NoSuchAlgorithmException {
		this.user.insertPartnerDB(fname, lname, email, PasswordEncryption.encryptPassword(password));
		this.user.insertPartnerKey(email);

	}
	
	public int insertIntoAddress(String email, String street, String province, String country, String zip, String phone,
			String city) throws SQLException {
		//return this.dao.insertAddress(email, street, province, country, zip, phone, city);
		return this.address.insertAddress(email, street, province, country, zip, phone, city);
	}

	public String getCustomerName(String email) {

		//return this.dao.getCustomerName(email);
		return this.user.getCustomerName(email);
	}

	public String getPartnerName(String email) {

		//return this.dao.getPartnerName(email);
		 return this.user.getPartnerName(email);
	}
	
	public AddressBean getUserAddress(int cid) throws SQLException {
		  return this.address.retrieveAddressByCustomerId(cid);
	}
	
	public String getpartnerKey(String email) {
		return this.user.getpartnerKey(email);
	}
	
	public AddressBean retrieveAddress(String email) throws SQLException {
		//return this.dao.retrieveAddressByEmail(email);
		return this.address.retrieveAddressByEmail(email);
	}	  

	public UserBean retrieveUserInfo(String email) throws SQLException {
		//return this.dao.retrieveAllUserInfo(email);
		return this.user.retrieveAllUserInfo(email);
	}
	
	public boolean isValidPartnerKey(String key) {
		return this.user.isValidPartnerKey(key);
	}
	
	public boolean isEmailTaken(String email) {
		//return this.dao.isEmailTaken(email);
		return this.user.isEmailTaken(email);
	}
	
	public UserBean isUserExist(String email, String password) throws NoSuchAlgorithmException {
		 return this.user.isUserExist(email, PasswordEncryption.encryptPassword(password));
	}
	
}




