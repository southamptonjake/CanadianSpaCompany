package test;
import java.io.IOException;
import java.util.Map;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;



public final class Emailer {

	public static void orderRecieved(String email, LineItems li, Address a)
	{
		System.out.println("hi");
		String apikey = "***REMOVED***";
		Email from = new Email("test@example.com");
		String subject = "Sending with SendGrid is Fun";
		Email to = new Email("***REMOVED***");
		Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		Mail mail = new Mail(from, subject, to, content);

		mail.setTemplateId("d-520336ae738a41fcb33c8291584495a5");

		//mail.getPersonalization().get(0).addDynamicTemplateData("{name}", a.name());
		
		SendGrid sg = new SendGrid(apikey);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	public static void orderShipped(String email, LineItems li,Address a, String trackingURL, String trackingNumber, int stage)
	{

	}

	public static void holder(){

	}

}
