package bezbednosttim6.service;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;

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
		return userRepo.findUserByEmail(email).orElseThrow(()-> new ObjectNotFoundException("User not found."));
	}

	
	
	
}
