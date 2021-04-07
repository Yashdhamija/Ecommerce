package dao_tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import DAO.DBConnect;
import DAO.DatabaseConnection;
import DAO.UserDAO;
public class DatabaseConnectionTest {



	@Test
	public void testSuccessfulConnection() throws SQLException {
	    Connection con = DatabaseConnection.getInstance().getConnection();
        assertNotNull(con);
        assertTrue(con.isValid(0));
        con.close();
		
		
	}
	
	
	
	
	@Test(expected = SQLException.class)
	public void testQueryWithClosedConnection() throws SQLException {
	    
		DatabaseConnection dbConnection = DatabaseConnection.getInstance();
		String bid = "121-3-3434-4545-5";
		String query = "SELECT * FROM Book WHERE bid=?";

		PreparedStatement ps = dbConnection.getConnection().prepareStatement(query);
		ps.close();
		ps.setString(1,bid);
		ResultSet rs = ps.executeQuery();
       
      		
		
	}
	
	
	
	
}
