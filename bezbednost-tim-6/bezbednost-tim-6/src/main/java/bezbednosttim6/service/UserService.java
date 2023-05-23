package bezbednosttim6.service;


import java.util.Date;
import java.util.List;


import bezbednosttim6.mapper.UserDTOwithPasswordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
			throw new RuntimeException();
		}

		User user = mapper.fromDTOtoUser(userRequest);
		user.setActivated(false);
		user.setEmail(userRequest.getEmail());
		user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(roleService.findById(2));

		user = addUser(user);
		if(userRequest.getValidationType().equals("emailValidation")) {
			String token = activationService.generateActivation(userRequest.getEmail());
			try {
				mailService.sendActivationEmail(userRequest.getEmail(), token);
			} catch (MessagingException e) {
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
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return new SuccessDTO("Successfully resent activation!");
	}

	public String refreshToken(LoginResponseDTO dto) {
		
		String accessToken = dto.getAccessToken();
		String refreshToken = dto.getRefreshToken();
		
		if(jwtTokenUtils.getExpirationDateFromToken(refreshToken).after(new Date())) {
			throw new RuntimeException("RefreshToken Expired");
		}
				
		if(!jwtTokenUtils.getUsernameFromToken(accessToken).equals(jwtTokenUtils.getUsernameFromToken(refreshToken))) {
			throw new RuntimeException("Usernames don't match!");
		}
		
		String token = jwtTokenUtils.generateToken(jwtTokenUtils.getUsernameFromToken(refreshToken));
		return token;
		
	}
	
}
