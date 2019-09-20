package shared;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import api.APIKEYS;

public class Customer {
	String email,phone,mobile,firstName,lastName,company,addr1,addr2,city,country,state,zip ;

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
		
		checkPostcode();
	
		
	}
	
	
	public void checkPostcode()
	{
		Client client = ClientBuilder.newClient();
		Response response = client.target("https://ws.postcoder.com/pcw/" + APIKEYS.postcoderApi + "/address/uk/" + "RH107GH")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		System.out.println(response.readEntity(String.class));

		

	}
}