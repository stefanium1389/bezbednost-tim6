package bezbednosttim6.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bezbednosttim6.dto.SuccessDTO;
import bezbednosttim6.exception.ActionExpiredException;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.model.Activation;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.ActivationRepository;
import bezbednosttim6.repository.UserRepository;

@Service
public class ActivationService {
	
	@Autowired
	private ActivationRepository activationRepo;
	@Autowired
	private UserRepository userRepo;
	
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

	public String generateActivation(String email) {
		Activation activation = new Activation();
		activation.setEmail(email);
		String token = this.generateToken();
		activation.setToken(token);
		activation.setTimestamp(new Date(System.currentTimeMillis()));
		activation.setExpires(new Date(System.currentTimeMillis()+(EXPIRES*1000)));
		activationRepo.save(activation);
		activationRepo.flush();
		return token;
	}
	
	public String generateSMSActivation(String email) {
		Activation activation = new Activation();
		activation.setEmail(email);
		String code = this.generateSMSCode();
		activation.setToken(code);
		activation.setTimestamp(new Date(System.currentTimeMillis()));
		activation.setExpires(new Date(System.currentTimeMillis()+(EXPIRES*1000)));
		activationRepo.save(activation);
		activationRepo.flush();
		return code;
	}
	
	public SuccessDTO activatePassenger(String token)
	{
		Optional<Activation> activation = activationRepo.findActivationByToken(token);
		if(activation.isEmpty()) {
			throw new ObjectNotFoundException("Activation doesn't exist");
		}
		Activation actual = activation.get();
		if(actual.getExpires().before(new Date(System.currentTimeMillis()))) {
			throw new ActionExpiredException("Activation expired");
		}
		User user = userRepo.findUserByEmail(actual.getEmail());
		if(user==null) {
			activationRepo.delete(actual);
			activationRepo.flush();
			throw new ObjectNotFoundException("Email doesn't exist!");
		}
		user.setActivated(true);
		userRepo.save(user);
		activationRepo.delete(actual);
		userRepo.flush();
		activationRepo.flush();
		SuccessDTO dto = new SuccessDTO("Successful account activation!");
		return dto;
	}
	
	public List<String> regenerateActivation(String token) {
		Optional<Activation> activation = activationRepo.findActivationByToken(token);
		if(activation.isEmpty()) {
			throw new ObjectNotFoundException("Activation doesn't exist");
		}
		List<String> lista = new ArrayList<String>();
		String newToken = this.generateActivation(activation.get().getEmail());
		lista.add(newToken);
		lista.add(activation.get().getEmail());
		return lista;
		
	}
	
}
