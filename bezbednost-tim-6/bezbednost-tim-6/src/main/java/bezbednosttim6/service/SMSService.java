package bezbednosttim6.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@Service
public class SMSService {
	
	@Value("${spring.twilio.sid}")
    private String sid;
	
	@Value("${spring.twilio.token}")
    private String token;
	
	@Value("${spring.twilio.number}")
    private String number;
	
	
	@Async
	public void sendSMS(String to, String body) {
		
		Twilio.init(sid, token);
		Message m = Message.creator(
				new PhoneNumber(to),
				new PhoneNumber(number),
				body
		).create();
		
		System.out.println(m.getSid());
	}
	
	public void sendActivationSMS(String toNumber, String code) {		
		String body = "Your IB account activation code: " + code;
		sendSMS(toNumber,body);
	}
	public void sendPasswordResetSMS(String toNumber, String code) {		
		String body = "Your password reset code: " + code;
		sendSMS(toNumber,body);
	}
	
//	public void sendPasswordResetMail(String email, String token) throws MessagingException {
//		String body = "To change your password click on the following link http://localhost:4200/reset-password?token="+token;
//		sendEmail(email,"IB Projekat Tim6 Reset Password",body);
//	}
}
