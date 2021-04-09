package dao_tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.junit.Test;
import DAO.AddressDAO;
import DAO.UserDAO;
import bean.AddressBean;

public class AddressDAOTest {

	@Test(expected = SQLIntegrityConstraintViolationException.class)
	public void testInsertAddressWithoutRegisteredUser() throws SQLException {
		
		AddressDAO address = new AddressDAO();
		String email = "jacob@gmail.com";
		String street = "21 Main Street";
		String province =  "Ontario";
		String country = "Canada";
		String zip = "M1W-2C8";
		String phone = "647-675-4567";
		String city = "Toronto";
		address.insertAddress(email, street, province, country,zip,phone,city);

	}
	
	
	
	@Test
	public void testInsertAddressSuccesful() throws SQLException {
		
		 UserDAO newUser = new UserDAO();
		 AddressDAO newUserAddress = new AddressDAO();
		 String fname = "Mark";
		 String lname = "Price";
		 String email = "mark@gmail.com";
		 String password = "Toronto";
		 String street = "21 Main Street";
		 String province =  "Ontario";
		 String country = "Canada";
		 String zip = "M1W-2C8";
		 String phone = "647-675-4567";
		 String city = "Toronto";
		 newUser.insertUserDB(fname, lname, email, password);
		 int successful = newUserAddress.insertAddress(email, street, province, country,zip,phone,city);
		 assertEquals(successful,1);
	}
	
	
	
	
	@Test
	public void testAddressRetrieval() throws SQLException {
		
		
		 AddressDAO addressDb = new AddressDAO();
		 AddressBean markAddressFromDB;
		 String fname = "Mark";
		 String lname = "Price";
		 String email = "mark@gmail.com";
		 String password = "Toronto";
		 String street = "21 Main Street";
		 String province =  "Ontario";
		 String country = "Canada";
		 String zip = "M1W-2C8";
		 String phone = "647-675-4567";
		 String city = "Toronto";
		 AddressBean actualMarkAddress =  new AddressBean(country, province,city,street,zip,phone);
	
		 markAddressFromDB =  addressDb.retrieveAddressByEmail(email);
		 assertEquals(actualMarkAddress.getCity(),markAddressFromDB.getCity());
		 assertEquals(actualMarkAddress.getCountry(),markAddressFromDB.getCountry());
		 assertEquals(actualMarkAddress.getPhone(),markAddressFromDB.getPhone());
		 assertEquals(actualMarkAddress.getProvince(),markAddressFromDB.getProvince());
		 assertEquals(actualMarkAddress.getStreet(),markAddressFromDB.getStreet());
		 assertEquals(actualMarkAddress.getZip(),markAddressFromDB.getZip());
	}
	
	
	

}
