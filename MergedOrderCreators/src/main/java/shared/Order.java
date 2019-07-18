package shared;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.APIKEYS;


public class Order {

	String APIKEY = APIKEYS.veeqoApi;
	public ArrayList<String> quanity;
	public ArrayList<String> sku;
	public ArrayList<String> price;
	public ArrayList<String> tax;
	public String notes;
	public String billingID;
	public Customer customer;
	public String channelID;

	public Order(ArrayList<String> quanity, ArrayList<String> sku, ArrayList<String> price,
			ArrayList<String> tax, String notes, Customer customer, String billingID, String channelID ) {
		super();
		this.quanity = quanity;
		this.sku = sku;
		this.price = price;
		this.tax = tax;
		this.notes = notes;
		this.customer = customer;
		this.billingID = billingID;
		this.channelID = channelID;
		
	}
	
	public void uploadOrder()
	{

		Client client = ClientBuilder.newClient();
		String lineItemAttributes = "";
		for(int i =0 ; i < sku.size() -1; i ++)
		{

			lineItemAttributes +=  "            {\r\n" + 
					"                \"quantity\": "+quanity.get(i)+",\r\n" + 
					"                \"sellable_id\": "+convertToSellableID(sku.get(i))+",\r\n" + 
					"                \"price_per_unit\": "+price.get(i)+",\r\n" + 
					"                \"tax_rate\": "+tax.get(i)+"\r\n" + 
					"            },\r\n";
		}
		lineItemAttributes +=  "            {\r\n" + 
				"                \"quantity\": "+quanity.get(quanity.size() -1)+",\r\n" + 
				"                \"sellable_id\": "+convertToSellableID(sku.get(quanity.size() -1))+",\r\n" + 
				"                \"price_per_unit\": "+price.get(quanity.size() -1)+",\r\n" + 
				"                \"tax_rate\": "+tax.get(quanity.size() -1)+"\r\n" + 
				"            }\r\n";
		Entity payload = Entity.json("\r\n" + 
				"{\r\n" + 
				"    \"order\": {\r\n" + 
				"        \"channel_id\":" + channelID + ",\r\n" + 
				"        \"customer_id\":"+ billingID + ",\r\n" + 
				"        \"deliver_to_attributes\": {\r\n" + 
				"            \"address1\": \""+customer.addr1+"\",\r\n" + 	
				"            \"address2\": \""+customer.addr2+"\",\r\n" + 
				"            \"city\": \""+customer.city+"\",\r\n" + 
				"            \"company\": \""+customer.company+"\",\r\n" + 
				"            \"country\": \""+customer.country+"\",\r\n" + 
				"            \"customer_id\": 7014549,\r\n" + 
				"            \"first_name\": \""+customer.firstName+"\",\r\n" + 
				"            \"last_name\": \""+customer.lastName+"\",\r\n" + 
				"            \"phone\": \""+customer.phone+"\",\r\n" + 
				"            \"state\": \""+customer.state+"\",\r\n" + 
				"            \"zip\": \""+customer.zip+"\"\r\n" + 
				"        },\r\n" + 
				"        \"line_items_attributes\": [\r\n" + 
				lineItemAttributes +
				"        ],\r\n" +
				"\"customer_note_attributes\": {\r\n" +
				"        \"text\": \"" + customer.email + " " + " notes " + "\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"}");
		Response response = client.target("https://api.veeqo.com/orders")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.post(payload);

		System.out.println(payload.getEntity().toString());
		//System.out.println(response.readEntity(String.class));
	}

	public String convertToSellableID(String sku)
	{

		Client client = ClientBuilder.newClient();
		Response response = client.target("https://api.veeqo.com/products?query=" + sku)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.get();
		String body = response.readEntity(String.class);
		body = body.substring(body.indexOf("\"sku_code\":\"" + sku + "\""));
		String sellableID = body.substring(body.indexOf("sellable_id") + 13,body.indexOf(",",body.indexOf(("sellable_id"))));
		System.out.println("sellable id " + sellableID);
		return sellableID;

	}

	public String getShippingID(String customerID)
	{
		Client client = ClientBuilder.newClient();
		Response response = client.target("https://api.veeqo.com/customers/" + customerID)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.get();
		String body = response.readEntity(String.class);
		//9477189
		String shippingAddress = body.substring(body.indexOf("billing_address"));
		String shippingID = shippingAddress.substring(shippingAddress.indexOf("id") + 4,shippingAddress.indexOf(",",shippingAddress.indexOf(("id"))));

		System.out.println("shipping id " + shippingID);

		return shippingID;
	}

}
