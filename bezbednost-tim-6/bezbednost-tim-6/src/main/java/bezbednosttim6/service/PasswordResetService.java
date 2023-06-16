package bezbednosttim6.service;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bezbednosttim6.dto.ChangePasswordRequestDTO;
import bezbednosttim6.dto.CodeAndPasswordDTO;
import bezbednosttim6.dto.PasswordResetRequestDTO;
import bezbednosttim6.dto.SuccessDTO;
import bezbednosttim6.exception.ConditionNotMetException;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.model.PasswordReset;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.PasswordResetRepository;
import bezbednosttim6.repository.UserRepository;
import jakarta.mail.MessagingException;


@Service
public class PasswordResetService {
	
	@Autowired
	private PasswordResetRepository passwordResetRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private MailingService mailService;
	@Autowired
	private SMSService smsService;
	@Autowired
	private PasswordEncoder encoder;
	
	private static int EXPIRES = 60*60*24; //1 dan
	
	
	private String generateToken() {
		UUID uuid = UUID.randomUUID();
		String token = uuid.toString();
		return token;
	}
	private String generateSMSCode() {
		Random r = new Random();
		int code = r.nextInt(100000, 999999);
		return String.valueOf(code);
	}
	
	private String generatePasswordResetRequest(String email) {
		User user = userRepo.findUserByEmail(email);
		if(user == null) {
			throw new ObjectNotFoundException("User does not exist, please register!");
		}
		PasswordReset reset = new PasswordReset();
		reset.setEmail(email);
		String token = this.generateToken();
		reset.setToken(token);
		reset.setMode("email");
		reset.setTimestamp(new Date(System.currentTimeMillis()));
		reset.setExpires(new Date(System.currentTimeMillis()+(EXPIRES*1000)));
		passwordResetRepo.save(reset);
		passwordResetRepo.flush();
		return token;
	}
	private String generatePasswordResetRequestSMS(String phone) {
		User user = userRepo.findUserByTelephoneNumber(phone);
		if(user == null) {
			throw new ObjectNotFoundException("User does not exist, please register!");
		}
		PasswordReset reset = new PasswordReset();
		reset.setEmail(phone);
		String code = generateSMSCode();
		reset.setToken(code);
		reset.setMode("sms");
		reset.setTimestamp(new Date(System.currentTimeMillis()));
		reset.setExpires(new Date(System.currentTimeMillis()+(EXPIRES*1000)));
		passwordResetRepo.save(reset);
		passwordResetRepo.flush();
		return code;
	}

	public SuccessDTO postPasswordReset(PasswordResetRequestDTO dto) {
		
		if(dto.getMode().equals("email")) {
			String email = dto.getEmail();
			String token = generatePasswordResetRequest(email);
			try {
				mailService.sendPasswordResetMail(email, token);
			} 
			catch (IOException e) {
				throw new ConditionNotMetException("Problem with email sending!");
			}
		}
		else if(dto.getMode().equals("phone")) {
			String phoneNumber = dto.getEmail();
			String code = generatePasswordResetRequestSMS(phoneNumber);
			try {
				smsService.sendPasswordResetSMS("+381" + phoneNumber, code);
			}
			catch (Exception e) {
				throw new ConditionNotMetException("Problem with SMS sending!");
			}
		}
		return new SuccessDTO("Successfully sent!");
		
	}

	public SuccessDTO putPasswordReset(CodeAndPasswordDTO dto) {
		Optional<PasswordReset> prOpt = passwordResetRepo.findByToken(dto.getCode());
		if(prOpt.isEmpty()) {
			throw new ObjectNotFoundException("Code is incorrect!");
		}
		PasswordReset actual = prOpt.get();
		if(actual.getExpires().before(new Date(System.currentTimeMillis()))) {
			throw new ConditionNotMetException("Code has expired!");
		}
		User user = null;
		if(actual.getMode().equals("email")) {
			user = userRepo.findUserByEmail(actual.getEmail());
		}
		if(actual.getMode().equals("sms")) {
			user = userRepo.findUserByTelephoneNumber(actual.getEmail());
		}
		if(user == null) {
			throw new ObjectNotFoundException("User does not exist!");
		}
		if(!dto.getNewPassword().equals(dto.getRepeatPassword())) {
			throw new ConditionNotMetException("Passwords not matching!");
		}
		String password = encoder.encode(dto.getNewPassword());
		user.setPassword(password);
		userRepo.save(user);
		userRepo.flush();
		passwordResetRepo.delete(actual);
		passwordResetRepo.flush();
		
		return new SuccessDTO("Password successfully changed!");	
	}

//	public SuccessDTO putChangePassword(Long id, ChangePasswordRequestDTO dto) {
//		User actual = userService.findUserById(id);
//		String encoded = encoder.encode(dto.getOldPassword());
//		if(!encoded.equals(actual.getPassword())) {
//			System.err.println("stara iz korisnika-"+actual.getPassword());
//			System.err.println("stara iz dto-"+encoded +" neenkodirana "+dto.getOldPassword());
//			System.err.println("nova-"+encoder.encode(dto.getOldPassword())+" neenkodirana "+dto.getNewPassword());
//			throw new ConditionNotMetException("Current password is not matching!");
//		}
//		actual.setPassword(encoder.encode(dto.getNewPassword()));
//		userRepo.save(actual);
//		userRepo.flush();
//		return new SuccessDTO("Password successfully changed!");
//	}
}
