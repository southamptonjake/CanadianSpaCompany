import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class Order {

	String APIKEY = "***REMOVED***";
	ArrayList<String> quanity,sku,price,tax;
	String orderNumber;
	Customer customer;
	String salesOrderNumber;
	String siteCode;
	String buyerName;
	String poType;


	public Order(ArrayList<String> quanity, ArrayList<String> sku, ArrayList<String> price,
			ArrayList<String> tax, String orderNumber, String salesOrderNumber, Customer customer, String siteCode, String buyerName, String poType) {
		super();
		this.quanity = quanity;
		this.sku = sku;
		this.price = price;
		this.tax = tax;
		this.orderNumber = orderNumber;
		this.customer = customer;
		this.salesOrderNumber = salesOrderNumber;
		this.siteCode = siteCode;
		this.buyerName = buyerName;
		this.poType = poType;
	}
	public void upload()
	{
		System.out.println(buyerName);
		uploadOrder(findCustomer());
	}
	
	public String findCustomer()
	{
		if(poType.equals("DC Manual PO"))
		{
			return "23678611";
		}
		else
		{
			if(buyerName.equals("B&Q plc"))
			{
				return "23657440";
			}
		}
		return "123";
		
	}
	
	public class CustomerHolder
	{
		Customer customer;

		public CustomerHolder(Customer customer) {
			super();
			this.customer = customer;
		}
		
		
	}
	public String uploadCustomer()
	{

		
		Client client = ClientBuilder.newClient();
		Gson gson = new Gson();
		String json = gson.toJson(new CustomerHolder(customer));
		Entity payload = Entity.json(json);
		System.out.println(payload.getEntity().toString());
		
		Response response = client.target("https://api.veeqo.com/customers")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.post(payload);

		String body = response.readEntity(String.class);
		
		System.out.println(body);

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
				"        \"channel_id\": 48307,\r\n" + 
				"        \"customer_id\": "+ customerID + ",\r\n" + 
				"        \"deliver_to_id\": "+ uploadCustomer() + ",\r\n" + 
				"        \"line_items_attributes\": [\r\n" + 
				lineItemAttributes +
				"        ],\r\n" +
				"\"customer_note_attributes\": {\r\n" +
				"        \"text\": \""+orderNumber + " " + salesOrderNumber + " " + siteCode + "\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"}");
		Response response = client.target("https://api.veeqo.com/orders")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.post(payload);

		System.out.println(payload.getEntity().toString());
		System.out.println(response.readEntity(String.class));
	}

	public String convertToSellableID(String sku)
	{


		Client client = ClientBuilder.newClient();
		Response response;

		response = client.target("https://api.veeqo.com/products?query=" + sku)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.get();
		String body = response.readEntity(String.class);
		JsonArray jsonArray = new JsonArray();
		JsonParser jparse = new JsonParser();
		jsonArray = jparse.parse(body).getAsJsonArray();
		for(JsonElement j : jsonArray)
		{
			JsonArray sellableArray = j.getAsJsonObject().get("sellables").getAsJsonArray();
			for(JsonElement e : sellableArray)
			{
				if(e.getAsJsonObject().get("sku_code").getAsString().equals(sku)) {
					return e.getAsJsonObject().get("id").getAsString();
				}
			}
		}
		return sku;

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
