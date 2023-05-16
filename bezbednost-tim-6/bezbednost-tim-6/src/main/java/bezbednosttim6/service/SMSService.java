package bezbednosttim6.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SMSService {
	
	@Value("${spring.mail.username}")
    private String from;
	
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Async
	public void sendEmail(String to, String subject, String body) throws MessagingException {
	    MimeMessage message = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    helper.setFrom(from); // <--- THIS IS IMPORTANT FOR OUTLOOK!
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(body);
	    javaMailSender.send(message);
	}
	
	public void sendActivationEmail(String email, String token) throws MessagingException {		
		String body = "To verify your email click on the following link http://localhost:4200/activate?token="+token;
		sendEmail(email,"IB Projekat Tim6 Email Validation",body);
	}
	
	public void sendPasswordResetMail(String email, String token) throws MessagingException {
		String body = "To change your password click on the following link http://localhost:4200/reset-password?token="+token;
		sendEmail(email,"IB Projekat Tim6 Reset Password",body);
	}
}
