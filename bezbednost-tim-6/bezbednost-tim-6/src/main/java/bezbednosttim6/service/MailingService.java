package bezbednosttim6.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailingService {
	
	@Value("${spring.mail.username}")
    private String from;
	
	@Value("${sendgrid.api.key}")
    private String sendGridApiKey;
	
	@Async
	public void sendEmail(String to, String subject, String body) throws IOException {
		Email fromEmail = new Email(from);
		Email toEmail = new Email(to);
		Content content = new Content("text/plain",body);
		Mail mail = new Mail(fromEmail, subject, toEmail, content);
		SendGrid sg = new SendGrid(sendGridApiKey);
		Request request = new Request();
		request.setMethod(Method.POST);
	    request.setEndpoint("mail/send");
	    request.setBody(mail.build());
	    Response response = sg.api(request);
	}
	
	public void sendActivationEmail(String email, String token) throws IOException {		
		String body = "To verify your email click on the following link https://localhost:4200/activate?token="+token;
		sendEmail(email,"IB Projekat Tim6 Email Validation",body);
	}
	
	public void sendPasswordResetMail(String email, String token) throws IOException {
		String body = "To change your password click on the following link https://localhost:4200/reset-password?token="+token;
		sendEmail(email,"IB Projekat Tim6 Reset Password",body);
	}

	public void sendPasswordRenewMail(String email, String token) throws IOException {
		String body = "To renew your password click on the following link https://localhost:4200/renew-password?token="+token;
		sendEmail(email,"IB Projekat Tim6 Renew Password",body);
	}
}
