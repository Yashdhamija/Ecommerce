package bean;

public class OrderBean {

    private String orderId;
    private String lname;
    private String fname;
    private String status;
    private String address;
    private String date;
    private String bid;
    private int quantity;
    private int unitPrice;

    public OrderBean(String orderId, int quantity, int unitPrice, String lname, String fname, String status,
            String address, String date) {

        this.orderId = orderId;
        this.lname = lname;
        this.fname = fname;
        this.status = status;
        this.address = address;
        this.date = date;
        this.quantity = quantity;
        this.unitPrice = unitPrice;

    }
    
    public String getBid() {

        return this.bid;
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

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

}