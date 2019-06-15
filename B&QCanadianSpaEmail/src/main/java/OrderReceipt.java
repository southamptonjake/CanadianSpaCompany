import java.io.IOException;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

public class OrderReceipt {

	String recipient;

	public OrderReceipt(String recipient) {
		String apikey = "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";
		Email from = new Email("noreply@canadianspacompany.com");
		String subject = "";
		Email to = new Email(recipient);
		Content content = new Content("text/plain", "");
		Mail mail = new Mail(from, subject, to, content);
		//mail.personalization.get(0).addSubstitution("-name-", "Example User");
		//mail.personalization.get(0).addSubstitution("-city-", "Denver");
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




}
