package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

	private static DatabaseConnection admin;

	public AdminDAO() throws SQLException {

		this.admin = DatabaseConnection.getInstance();

	}

	public boolean IsAdminValidated(String email, String password) {
		String dbEmail = null;
		String dbPassword = null;
		boolean isAdmin = false;

		try {
			this.admin = DatabaseConnection.getInstance();
			String query = "SELECT email,password FROM AdminBookStore WHERE email = ? AND password = ?";
			PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dbEmail = rs.getString("email");
				dbPassword = rs.getString("password");

			}

			if (dbEmail != null && dbPassword != null) {

				isAdmin = true;

			}

			rs.close();
			this.admin.getConnection().close();

		} catch (SQLException se) {
			se.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return isAdmin;
	}

	public void insertAdminIntoDB(String email, String fname, String lname, String password) throws SQLException {
		this.admin = DatabaseConnection.getInstance();
		String query = "INSERT INTO AdminBookStore VALUES(?,?,?,?)";
		PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
		ps.setString(1, email);
		ps.setString(2, lname);
		ps.setString(3, fname);
		ps.setString(4, password);
		int result = ps.executeUpdate();
		ps.close();
		this.admin.getConnection().close();
		// return result;
	}

	public String getAdminPwd(String password) {
		// password parameter should be encrypted by using
		// BookStoreModel.encryptPassword()

		String p = null;
		try {
			this.admin = DatabaseConnection.getInstance();
			String query = "SELECT password FROM AdminBookStore WHERE password = ?";
			PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
			ps.setString(1, password);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				p = rs.getString("password");
			}
			rs.close();
			this.admin.getConnection().close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public String getAdminEmail(String email) {

		String p = null;
		try {
			this.admin = DatabaseConnection.getInstance();
			String query = "SELECT email FROM AdminBookStore WHERE email = ?";
			PreparedStatement ps = this.admin.getConnection().prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				p = rs.getString("email");
			}
			rs.close();
			this.admin.getConnection().close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

}
