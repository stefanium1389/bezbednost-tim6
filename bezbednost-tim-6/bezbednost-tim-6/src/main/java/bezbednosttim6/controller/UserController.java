package bezbednosttim6.controller;

import bezbednosttim6.security.TokenUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bezbednosttim6.dto.ErrorDTO;
import bezbednosttim6.dto.LoginRequestDTO;
import bezbednosttim6.dto.LoginResponseDTO;
import bezbednosttim6.service.UserService;

import java.security.Principal;


@RestController
@RequestMapping("api/")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtils jwtTokenUtil;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@PostMapping ("user/login")
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
	@GetMapping("proba")
	public ResponseEntity<?> proba (Principal principal)
	{
		return new ResponseEntity<>(passwordEncoder.encode("admin"),HttpStatus.OK);
	}

}