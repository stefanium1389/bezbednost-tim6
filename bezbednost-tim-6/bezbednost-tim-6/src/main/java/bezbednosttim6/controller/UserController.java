package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.exception.ResourceConflictException;
import bezbednosttim6.mapper.UserDTOwithPasswordMapper;
import bezbednosttim6.model.User;
import bezbednosttim6.security.TokenUtils;
import bezbednosttim6.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bezbednosttim6.service.UserService;

import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("api/user/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtils jwtTokenUtil;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private UserDTOwithPasswordMapper mapper;
	
	
	@PostMapping ("login")
	public ResponseEntity<?> postLogin (@RequestBody LoginRequestDTO loginRequestDTO)
	{
		try
		{
		UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
				loginRequestDTO.getPassword());
		Authentication auth = authenticationManager.authenticate(authReq);

		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(auth);

		String token = jwtTokenUtil.generateToken(loginRequestDTO.getEmail());
		//String refreshToken = jwtTokenUtil.generateRefrshToken(loginRequestDTO.getEmail());
		LoginResponseDTO response = new LoginResponseDTO(token, token);
				
		return new ResponseEntity<LoginResponseDTO>(response,HttpStatus.OK);
		}
		catch(AuthenticationException e)
		{
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
		}
	}

	//@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("proba")
//	public ResponseEntity<?> proba (Principal principal)
//	{
//		return new ResponseEntity<>(passwordEncoder.encode("admin"),HttpStatus.OK);
//	}


	@PostMapping("register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDTO userRequest) throws UnsupportedEncodingException {
		User existUser = this.userService.findUserByEmail(userRequest.getEmail());
		if (existUser != null) {
			return new ResponseEntity<>(new ResourceConflictException(userRequest.getId(), "Username already exists").getMessage(), HttpStatus.BAD_REQUEST);
		} else {
			User user = mapper.fromDTOtoUser(userRequest);
			user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
			user.setRole(roleService.findById(2));

			user = userService.addUser(user);
			return new ResponseEntity<>(new RegisterResponseDTO(user), HttpStatus.CREATED);
		}
	}

}