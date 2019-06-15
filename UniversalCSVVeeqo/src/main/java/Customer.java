
public class Customer {
	String email,phone,mobile,firstName,lastName,company,addr1,addr2,city,country,state,zip;

	public Customer(String email, String phone, String mobile, String firstName, String lastName, String company,
			String addr1, String addr2, String city, String country,String state, String zip) {
		super();
		this.email = email;
		this.phone = phone;
		this.mobile = mobile;
		this.firstName = firstName;
		this.lastName = lastName;
		this.company = company;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.city = city;
		this.country = country;
		this.state = state;
		this.zip = zip;


		if(this.phone.equals("0000000000"))
		{
			this.phone = this.mobile;
		}
	}
}
