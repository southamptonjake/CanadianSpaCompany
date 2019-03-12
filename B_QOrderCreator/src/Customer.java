
public class Customer {
	String email,phone,mobile;
	Billing_Address_Attributes billing_address_attributes;
	Billing_Address_Attributes shipping_address_attributes;
	public Customer(String email, String phone, String mobile, String firstName, String lastName, String company,
			String addr1, String addr2, String city, String country,String state, String zip) {
		super();
		this.email = email;
		this.phone = phone;
		this.mobile = mobile;
		
		billing_address_attributes = new Billing_Address_Attributes(firstName,lastName,company,addr1,addr2,city,country,zip);
	}
	
	
	
	
	
}
