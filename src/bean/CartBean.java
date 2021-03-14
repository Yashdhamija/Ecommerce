package bean;

public class CartBean {
	
	int id;
	String bookid;
	int price;
	String title;
	int quantity;
	
	public CartBean(int id, String bid, int price, String title, int quantity) {
		this.id = id;
		this.bookid = bid;
		this.price = price;
		this.title = title;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
	}
