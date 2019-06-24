package test;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import test.NewOrderFinder.Allocations;
import test.NewOrderFinder.Shipment;
import test.NewOrderFinder.TrackingNumber;




public class NewOrder {
	
	
	public class Order{
		int id;
		CustomerNote customer_note;
		DeliverTo deliver_to;
		LineItems[] line_items;
	}
	
	public class CustomerNote
	{
		String text;
	}
	
	public class DeliverTo
	{
		String phone;
	}
	
	public class LineItems
	{
		int quantity;
		Sellable sellable;
		
	}
	
	public class Sellable
	{
		String product_title;
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		int lastId= 30375175;

		String APIKEY = "***REMOVED***";

		Client client = ClientBuilder.newClient();
		Response response = client.target("https://api.veeqo.com/orders?since_id="+lastId+"&tags=B%20%26%20Q")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.get();



		String body = response.readEntity(String.class);
		Gson g = new Gson();


		Order[] orders = g.fromJson(body, Order[].class);
		
		System.out.println(orders[0].line_items[0].sellable.product_title);
	}


}
