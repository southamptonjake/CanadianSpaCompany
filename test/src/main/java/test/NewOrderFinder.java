package test;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;


public class NewOrderFinder {
	
	
	public class Order{
		int id;
		
	}
	
	public static void main(String[] args) throws IOException {

		int lastId;
		
		String APIKEY = "***REMOVED***";

		Client client = ClientBuilder.newClient();
		Response response = client.target("https://api.veeqo.com/orders?created_at_min=2019-06-12%2011%3A10%3A01&tags=B%20%26%20Q")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.get();


		
		String body = response.readEntity(String.class);
		Gson g = new Gson();
		
		System.out.println(body);
		
		Order[] orders = g.fromJson(body, Order[].class);
		
		System.out.print(orders.length);
	}
}
