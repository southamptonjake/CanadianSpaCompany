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
			else if(buyerName.equals("B&Q Ireland Ltd"))
			{
				return "23677719";
			}
			else if(buyerName.equals("B&Q (Retail) Jersey Ltd"))
			{
				return "23677785";
			}
			else if(buyerName.equals("B&Q (Retail) Guernsey Ltd"))
			{
				return "23677822";
			}
		}
		return "-1";
		
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
				"        \"deliver_to_attributes\": {\r\n" + 
				"            \"address1\": \""+customer.addr1+"\",\r\n" + 	
				"            \"address2\": \""+customer.addr2+"\",\r\n" + 
				"            \"city\": \""+customer.city+"\",\r\n" + 
				"            \"company\": \""+customer.company+"\",\r\n" + 
				"            \"country\": \""+customer.country+"\",\r\n" + 
				"            \"customer_id\":" +customerID + ",\r\n" + 
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






}
