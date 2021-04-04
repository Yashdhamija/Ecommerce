package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import DAO.UserDAO;
import bean.AddressBean;

public class AddressDAO {
	
	
	private static DatabaseConnection address;
	private UserDAO user;
	
	public AddressDAO() throws SQLException {
		
		this.address =   DatabaseConnection.getInstance();
		this.user = new UserDAO();
		
	}
	
	
	
	public int insertAddress(String email, String street, String province, String country, String zip, String phone,
			String city) throws SQLException {
	
		String query = "INSERT INTO Address VALUES(?,?,?,?,?,?,?)";
		PreparedStatement ps = this.address.getConnection().prepareStatement(query);
		int customerid = this.user.retrieveCustomerId(email);
		ps.setInt(1, customerid);
		ps.setString(2, street);
		ps.setString(3, province);
		ps.setString(4, country);
		ps.setString(5, zip);
		ps.setString(6, phone);
		ps.setString(7, city);
		int result = ps.executeUpdate();
		ps.close();
		this.address.getConnection().close();
		return result;
	}
	
	
	public AddressBean retrieveAddressByEmail(String email) throws SQLException {
		int customerid = this.user.retrieveCustomerId(email);
		String street;
		String province;
		String city;
		String zip;
		String country;
		String phone;

		
		AddressBean address = null;

		String query = "SELECT * FROM Address WHERE cid=?";

		PreparedStatement ps = this.address.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			street = rs.getString("street");
			province = rs.getString("province");
			city = rs.getString("city");
			zip = rs.getString("zip");
			country = rs.getString("country");
			phone = rs.getString("phone");
			address = new AddressBean(country, province, city, street, zip, phone);
		}

		rs.close();
		ps.close();
		this.address.getConnection().close();
		return address;

	}
	
	// Use this one instead of above
	public AddressBean retrieveAddressByCustomerId(int cid) throws SQLException {
		
		
		String street;
		String province;
		String city;
		String zip;
		String country;
		String phone;

		
		AddressBean address = null;

		String query = "SELECT * FROM Address WHERE cid=?";

		PreparedStatement ps = this.address.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			street = rs.getString("street");
			province = rs.getString("province");
			city = rs.getString("city");
			zip = rs.getString("zip");
			country = rs.getString("country");
			phone = rs.getString("phone");
			address = new AddressBean(country, province, city, street, zip, phone);
		}

		rs.close();
		ps.close();
		this.address.getConnection().close();
		return address;

	}
	

	

}
