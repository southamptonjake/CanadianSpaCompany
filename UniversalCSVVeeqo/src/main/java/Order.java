import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class Order {

	String APIKEY = "***REMOVED***";
	ArrayList<String> quanity,sku,price,tax;
	//cell A (0)
	String orderNumber;
	//cell BE (56)
	String orderRef;
	//cell BL (63)
	String customerSKU;
	Customer customer;


	public Order(ArrayList<String> quanity, ArrayList<String> sku, ArrayList<String> price,
			ArrayList<String> tax, String orderNumber, String orderRef, String customerSKU, Customer customer) {
		super();
		this.quanity = quanity;
		this.sku = sku;
		this.price = price;
		this.tax = tax;
		this.orderNumber = orderNumber;
		this.orderRef = orderRef;
		this.customerSKU = customerSKU;
		this.customer = customer;
	}
	public void upload()
	{
		uploadOrder("12604329");
	}
	public String uploadCustomer()
	{

		Client client = ClientBuilder.newClient();
		Entity payload = Entity.json("{  \"customer\": { \"email\": \"maildata@smtp1.homebase.co.uk\",    \"phone\": \""+customer.phone+"\",    \"mobile\": \""+customer.mobile+"\",    \"billing_address_attributes\": {      \"first_name\": \"HomeBase\",      \"last_name\": \"\",      \"company\": \"HomeBase\",      \"address1\": \"489-499 Avebury Boulevard\",      \"address2\": \"\",      \"city\": \"Milton Keynes\",      \"country\": \""+customer.country+"\",      \"zip\": \"MK9 2NW\"    }  }}");	
		Response response = client.target("https://api.veeqo.com/customers")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.post(payload);

		String body = response.readEntity(String.class);

		String customerID = body.substring(body.indexOf("id") + 4,body.indexOf(","));
		System.out.println("customer id " + customerID);

		return customerID;
	}
	public void uploadOrder(String customerID)
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
				"        \"channel_id\": 46687,\r\n" + 
				"        \"customer_id\":"+ customerID + ",\r\n" + 
				"        \"deliver_to_attributes\": {\r\n" + 
				"            \"address1\": \""+customer.addr1+"\",\r\n" + 	
				"            \"address2\": \""+customer.addr2+"\",\r\n" + 
				"            \"city\": \""+customer.city+"\",\r\n" + 
				"            \"company\": \""+customer.company+"\",\r\n" + 
				"            \"country\": \""+customer.country+"\",\r\n" + 
				"            \"customer_id\": 11941510,\r\n" + 
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
				"        \"text\": \""+orderNumber + " " + orderRef + " " + customerSKU + "\"\r\n" + 
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

		System.out.println(sku);
		Client client = ClientBuilder.newClient();
		Response response = client.target("https://api.veeqo.com/products?query=" + sku)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.get();
		String body = response.readEntity(String.class);
		System.out.println(body);
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
