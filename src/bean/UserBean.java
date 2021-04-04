package bean;

public class UserBean {
	String fname;
	String lname;
	String email;
	String password;
	int customerId;
	int userType;
	// For Partners and Customers
	public UserBean(String fname, String lname, String email, String password, int customerId, int userType) {
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.password = password;
		this.customerId = customerId;
		this.userType = userType;

	}
	//For Administrator
	public UserBean(String fname, String lname, String email, String password) {
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.password = password;

	}

	


	public String getFirstname() {
		return fname;
	}

	public void setFirstname(String fname) { // If you change your name then makes sense
		this.fname = fname;
	}

	public String getLastname() {
		return lname;
	}

	public void setLastname(String lname) { // If you change your name then makes sense
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public int getUserType() {

		return this.userType;
	}

	public String getPassword() {

		return this.password;// encrypt it
	}

	public void setPassword(String password) {

		this.password = password;
	}

}
