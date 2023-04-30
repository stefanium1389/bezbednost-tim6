package bezbednosttim6.service;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bezbednosttim6.dto.RegisterRequestDTO;
import bezbednosttim6.dto.RegisterResponseDTO;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.exception.ResourceConflictException;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleService roleService;

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
		
		User user = new User();
		user.setActivated(false);
		user.setEmail(userRequest.getEmail());
		user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(roleService.findById(2));

		user = addUser(user);
		return user;
		
	}	
	
	
}
