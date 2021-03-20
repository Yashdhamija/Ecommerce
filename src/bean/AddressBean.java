package bean;

public class AddressBean {
	String country;
	String province;
	String city;
	String street;
	String zip;
	String phone;

	public AddressBean(String country, String province, String city, String street, String zip, String phone) {

		this.country = country;
		this.province = province;
		this.city = city;
		this.street = street;
		this.zip = zip;
		this.phone = phone;

	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
