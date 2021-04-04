package bean;

public class OrderBean {

    private String orderId;
    private String lname;
    private String fname;
    private String status;
    private int cid;
    private String date;
    private String bid;
    private int quantity;
    private int unitPrice;
    private String url;

    public OrderBean(String orderId, int quantity, int unitPrice, String lname, String fname, String status,
            int cid, String date, String bid, String url) {

        this.orderId = orderId;
        this.lname = lname;
        this.fname = fname;
        this.status = status;
        this.cid = cid;
        this.date = date;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.bid = bid;
        this.url = url;
    }
    
    public String getBid() {

        return this.bid;
    }
    
    public String getUrl() {
    	return this.url;
    }
    

    public int getQuantity() {

        return this.quantity;
    }

    public int getPrice() {

        return this.unitPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLname() {
        return lname;
    }

    public String getFname() {
        return fname;
    }

    public String getStatus() {
        return status;
    }

    public int getCid() {
        return cid;
    }

    public String getDate() {
        return date;
    }

}