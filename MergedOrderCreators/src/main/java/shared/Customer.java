package shared;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import javax.swing.JOptionPane;
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

	class Postcoder
	{
		String premise,street,dependentlocality,posttown,county;


		public String addr1() {
			return premise + " " + street;
		}

		public void convertNulls()
		{
			if(premise==null)
			{
				premise = "";
			}
			if(street==null)
			{
				street = "";
			}
			if(dependentlocality==null)
			{
				dependentlocality = "";
			}
			if(posttown==null)
			{
				posttown = "";
			}
			if(county==null)
			{
				county = "";
			}
		}
	}

	public String getNumber()
	{
		return addr1.split(" ")[0];
	}

	public void checkPostcode()
	{


		Client client = ClientBuilder.newClient();
		Response response = client.target("https://ws.postcoder.com/pcw/" + APIKEYS.postcoderApi + "/address/uk/" + zip + "/" + getNumber())
				.request(MediaType.APPLICATION_JSON_TYPE)
				.get();





		String body = response.readEntity(String.class);
		System.out.println(body);
		Gson g = new Gson();


		Postcoder coder = g.fromJson(body, Postcoder[].class)[0];


		coder.convertNulls();
		fillEmpties(coder);

		if(differences(coder))
		{
			int reply = JOptionPane.showConfirmDialog(null, createMessageDialog(coder), "Choose Address", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "HELLO");
			}
			else {
				JOptionPane.showMessageDialog(null, "GOODBYE");
				System.exit(0);
			}

		}
	}


	public void fillEmpties(Postcoder p)
	{
		if(addr1.equals(""))
		{
			addr1 = p.addr1();
		}
		if(addr2.equals(""))
		{
			addr2 = p.dependentlocality;
		}
		if(city.equals(""))
		{
			city = p.posttown;
		}
		if(state.equals(""))
		{
			state = p.county;
		}
	}


	public boolean differences(Postcoder p)
	{
		if(!addr1.equals(p.addr1()))
		{
			return true;
		}
		if(!addr2.equals(p.dependentlocality))
		{
			return true;
		}
		if(!city.equals(p.posttown))
		{
			return true;
		}
		if(!state.equals(p.county))
		{
			return true;
		}
		return false;
	}


	public String createMessageDialog(Postcoder p)
	{
		String result = "";
		result += "Supplied Address:" + "\r\n";
		result += addr1 + "\r\n";
		result += addr2 + "\r\n";
		result += city + "\r\n";
		result += state + "\r\n";
		result += zip + "\r\n";

		result += "\r\n";
		result += "\r\n";

		result += "Corrected Address:" + "\r\n";
		result += p.addr1() + "\r\n";
		result += p.dependentlocality + "\r\n";
		result += p.posttown + "\r\n";
		result += p.county + "\r\n";
		result += zip + "\r\n";

		result += "\r\n";
		result += "Press Yes to change to corrected address, Press No to revert to supplied address";

		return result;
	}







}
