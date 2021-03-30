package bean;

public class UserBean {
	String fname;
	String lname;
	String email;
	
	
	public UserBean(String fname, String lname, String email) {
		this.fname = fname;
		this.lname = lname;
		this.email = email;
	}


	public String getFirstname() {
		return fname;
	}


	public void setFirstname(String fname) {
		this.fname = fname;
	}


	public String getLastname() {
		return lname;
	}


	public void setLastname(String lname) {
		this.lname = lname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
}
