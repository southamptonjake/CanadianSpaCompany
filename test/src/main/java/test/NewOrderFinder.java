package test;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import test.NewOrder.Sellable;


public class NewOrderFinder {


	public class Order{
		int id;
		Allocations[] allocations;
		String allocated_completly;
	}

	public class Allocations{
		Shipment shipment;
		LineItems[] line_items;
	}

	public class Shipment{
		int id;
		TrackingNumber tracking_number;
		String tracking_url;
	}

	public class TrackingNumber{
		String tracking_number;
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

		int orderId= 30375175;

		String APIKEY = "***REMOVED***";

		Client client = ClientBuilder.newClient();
		Response response = client.target("https://api.veeqo.com/orders/" + orderId)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.get();



		String body = response.readEntity(String.class);
		Gson g = new Gson();


		Order orders = g.fromJson(body, Order.class);
		
		System.out.println(orders.id);
	}
}
