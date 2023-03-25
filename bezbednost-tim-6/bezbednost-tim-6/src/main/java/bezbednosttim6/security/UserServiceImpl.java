package bezbednosttim6.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bezbednosttim6.exception.UserNotActivatedException;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.UserRepository;


@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository allUsers;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> ret = allUsers.findUserByEmail(username);
		if (!ret.isEmpty() ) {
			if(!ret.get().isActivated()) {
				throw new UserNotActivatedException("User is not activated with this username: "+username);
			}
			
			return org.springframework.security.core.userdetails.User
					.withUsername(username)
					.password(ret.get().getPassword())
					.roles(ret.get().getRole().toString())
					.build();
		}
		throw new UsernameNotFoundException("User not found with this username: " + username);
	}

}
