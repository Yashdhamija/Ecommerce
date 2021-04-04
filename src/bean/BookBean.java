package bean;

public class BookBean {
	String bid;
	String title;
	int price;
	String category;
	String url;
	
	public BookBean(String bid, String title, int price, String category, String url) {
		this.bid = bid;
		this.title = title;
		this.price = price;
		this.category = category;
		this.url = url;
	}
	
	public BookBean() {
		
		
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}
