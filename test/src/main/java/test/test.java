package test;
import java.io.IOException;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

public class test {

	public static void main(String[] args) throws IOException {
		String apikey = "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";
		Email from = new Email("jal2g15@soton.ac.uk");
		String subject = "Sending with SendGrid is Fun";
		Email to = new Email("***REMOVED***");
		Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		Mail mail = new Mail(from, subject, to, content);

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
			throw ex;


		}
	}
}
