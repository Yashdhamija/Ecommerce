package bean;

public class ReviewBean {
	String firstName;
	String lastname;
	String bid;
	String review;
	String title;
	
	public ReviewBean(String fname, String lname, String bid, String review, String title) {
		this.firstName = fname;
		this.lastname = lname;
		this.bid = bid;
		this.review = review;
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getBid() {
		return bid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	
	
}
