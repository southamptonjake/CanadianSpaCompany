import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import entities.HomebaseOrder;
import entities.LineItems;
import entities.Settings;


@WebServlet(
		name = "FindUpdates",
		urlPatterns = {"/fu"}
		)
public class FindUpdates extends HttpServlet {

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

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {



		ObjectifyService.register(HomebaseOrder.class); 
		ObjectifyService.register(Settings.class); 
		ObjectifyService.register(LineItems.class); 



		Settings s = ObjectifyService.ofy().load().type(Settings.class).first().now();


		Query<HomebaseOrder> q = ObjectifyService.ofy().load().type(HomebaseOrder.class);

		//get all the orders which have never had a allocations
		Query<HomebaseOrder> q0 = q.filter("stage", 0);
		List<HomebaseOrder> hos0 = q0.list();	
		//get all the orders which have had partial allocations
		Query<HomebaseOrder> q1 = q.filter("stage", 1);
		List<HomebaseOrder> hos1 = q1.list();	

		for(HomebaseOrder h: hos0)
		{
			
		}








		response.getWriter().println(hos1.size());

	}

	public Order idToClass(int orderId)
	{
		String APIKEY = "***REMOVED***";

		
		Client client = ClientBuilder.newClient();
		javax.ws.rs.core.Response response = client.target("https://api.veeqo.com/orders/" + orderId)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header("x-api-key", APIKEY)
				.get();



		String body = response.readEntity(String.class);
		Gson g = new Gson();


		return g.fromJson(body, Order.class);
	}
	

}