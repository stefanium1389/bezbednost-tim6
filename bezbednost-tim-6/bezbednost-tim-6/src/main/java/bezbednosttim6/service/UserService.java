package bezbednosttim6.service;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import bezbednosttim6.dto.RecaptchaResponse;
import bezbednosttim6.exception.TypeNotFoundException;
import bezbednosttim6.mapper.UserDTOwithPasswordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import bezbednosttim6.dto.LoginResponseDTO;
import bezbednosttim6.dto.RegisterRequestDTO;
import bezbednosttim6.dto.RegisterResponseDTO;
import bezbednosttim6.dto.SuccessDTO;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.exception.ResourceConflictException;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.UserRepository;
import bezbednosttim6.security.TokenUtils;
import jakarta.mail.MessagingException;
import org.springframework.web.client.RestTemplate;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserDTOwithPasswordMapper mapper;

	@Autowired
	private MailingService mailService;
	
	@Autowired
	private SMSService smsService;
	
	@Autowired
	private ActivationService activationService;
	
	@Autowired
	private TokenUtils jwtTokenUtils;

	@Value("${google.recaptcha.key.secret}")
	private String recaptchaSecretKey;
	
	@Value("${google.client.id}")
	private String APP_ID;
	
	public User addUser(User User) 
	{
		return userRepo.save(User);
	}
	
	public List<User> findAllUsers()
	{
		return userRepo.findAll();
	}
	
	public User updateUser(User User) 
	{
		return userRepo.save(User);
	}
	
	public void deleteUser(Long id) 
	{
		userRepo.deleteUserById(id);
	}
	
	public User findUserById (Long id) 
	{
		return userRepo.findUserById(id).orElseThrow(()-> new ObjectNotFoundException("User not found."));
	}
	
	public User findUserByEmail(String email) {
		return userRepo.findUserByEmail(email);
	}

	public User registerUser(RegisterRequestDTO userRequest) 
	{
		User existUser = findUserByEmail(userRequest.getEmail());
		if (existUser != null) {
			throw new RuntimeException("Username already exists");
		}

		User user = mapper.fromDTOtoUser(userRequest);
		user.setActivated(false);
		user.setEmail(userRequest.getEmail());
		user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(roleService.findById(2));
		if (userRequest.getValidationType().equals("emailValidation"))
		{
			user.setVerifyWithMail(true);
		}
		else if (userRequest.getValidationType().equals("phoneValidation"))
		{
			user.setVerifyWithMail(false);
		}
		else
		{
			throw new TypeNotFoundException("tip validacije nije prepoznat!");
		}

		user = addUser(user);
		if(userRequest.getValidationType().equals("emailValidation")) {
			String token = activationService.generateActivation(userRequest.getEmail());
			try {
				mailService.sendActivationEmail(userRequest.getEmail(), token);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(userRequest.getValidationType().equals("phoneValidation")) {
			String code = activationService.generateSMSActivation(userRequest.getEmail());
			
			try {
				smsService.sendSMS("+381"+userRequest.getTelephoneNumber(), code);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return user;
		
	}	
	
	public SuccessDTO resendActivation(String id) {
		
		List<String> lista = activationService.regenerateActivation(id);
		try {
			mailService.sendActivationEmail(lista.get(1), lista.get(0)); //lista[1] je mail, a lista[0] je token
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return new SuccessDTO("Successfully resent activation!");
	}

	public String refreshToken(LoginResponseDTO dto) {
		
		String accessToken = dto.getAccessToken();
		String refreshToken = dto.getRefreshToken();
		
		if(jwtTokenUtils.getExpirationDateFromToken(refreshToken).before(new Date(System.currentTimeMillis()))) {
			throw new RuntimeException("RefreshToken Expired");
		}
				
		if(!jwtTokenUtils.getUsernameFromToken(accessToken).equals(jwtTokenUtils.getUsernameFromToken(refreshToken))) {
			throw new RuntimeException("Usernames don't match!");
		}
		
		String token = jwtTokenUtils.generateToken(jwtTokenUtils.getUsernameFromToken(refreshToken));
		return token;
		
	}

	public boolean isValidCaptcha(String token) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + recaptchaSecretKey +"&response=" + token;
		RecaptchaResponse response = restTemplate.getForObject(url, RecaptchaResponse.class);
		return response != null && response.isSuccess() && response.getScore() >= 0.5;
	}
	public LoginResponseDTO loginWithGoogle(String credential) throws IOException, GeneralSecurityException {
		
		
		
		NetHttpTransport transport = new NetHttpTransport();
		JsonFactory jsonFactory = new GsonFactory();

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
		  .setAudience(Collections.singletonList(APP_ID))
		  .build();

		GoogleIdToken idToken = GoogleIdToken.parse(verifier.getJsonFactory(), credential);
		boolean tokenIsValid = (idToken != null) && verifier.verify(idToken);
		
		if(tokenIsValid) {
			GoogleIdToken.Payload payload = idToken.getPayload();
			String email = payload.getEmail();
			
			User u = userRepo.findUserByEmail(email);
			
			if(u == null) {
				User newU = new User();
				newU.setEmail(email);
				newU.setPassword(null);
				newU.setTelephoneNumber(null);
				newU.setActivated(true);
				newU.setName(null);
				newU.setSurname(null);
				newU.setRole(roleService.findById(2));
				userRepo.save(newU);
				userRepo.flush();
			}
			
			String accessToken = jwtTokenUtils.generateToken(email);
			String refreshToken = jwtTokenUtils.generateRefreshToken(email);
			LoginResponseDTO lr = new LoginResponseDTO(accessToken, refreshToken);
			return lr;
		}
		
		return null;
	}
	
}
