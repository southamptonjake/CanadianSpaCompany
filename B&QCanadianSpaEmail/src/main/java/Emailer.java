import java.io.IOException;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import entities.Address;
import entities.LineItems;

public final class Emailer {
	
	public static void orderRecieved(String email, LineItems li, Address a)
	{
		System.out.println();
		String apikey = "***REMOVED***";
		Email from = new Email("noreply@canadianspacompany.com");
		String subject = "";
		Email to = new Email("***REMOVED***");
		Content content = new Content("text/plain", "");
		Mail mail = new Mail(from, subject, to, content);
		mail.personalization.get(0).addSubstitution("-name-", a.name());
		mail.personalization.get(0).addSubstitution("-address-", a.toString());
		mail.personalization.get(0).addSubstitution("-lineItems-", a.toString());
		mail.setTemplateId("d-520336ae738a41fcb33c8291584495a5");
		SendGrid sg = new SendGrid(apikey);
		Request emailrequest = new Request();
		try {
			emailrequest.setMethod(Method.POST);
			emailrequest.setEndpoint("mail/send");
			emailrequest.setBody(mail.build());
			Response emailresponse = sg.api(emailrequest);
			System.out.println(emailresponse.getStatusCode());
			System.out.println(emailresponse.getBody());
			System.out.println(emailresponse.getHeaders());

		} catch (IOException ex) {
			
			ex.getStackTrace();

		}
	}
	
	public static void orderShipped(String email, LineItems li,Address a, String trackingURL, String trackingNumber, int stage)
	{
		
	}
	
	public static void holder(){
		
	}

}
