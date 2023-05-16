package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.exception.ActionExpiredException;
import bezbednosttim6.exception.ConditionNotMetException;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.exception.ResourceConflictException;
import bezbednosttim6.mapper.UserDTOwithPasswordMapper;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.User;
import bezbednosttim6.security.TokenUtils;
import bezbednosttim6.service.ActivationService;
import bezbednosttim6.service.PasswordResetService;
import bezbednosttim6.service.RoleService;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bezbednosttim6.service.UserService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@RestController
@RequestMapping("api/user/")
public class UserController {

	@Autowired
	private UserService userService;

	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtils jwtTokenUtil;

	@Autowired
	private ActivationService activationService;
		
	
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
		try {
			User newUser = userService.registerUser(userRequest);
			return new ResponseEntity<>(new RegisterResponseDTO(newUser) , HttpStatus.CREATED);
		}
		catch(RuntimeException e) {
			return new ResponseEntity<>(new ResourceConflictException(userRequest.getId(), "Username already exists").getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/activate/{activationId}")
    public ResponseEntity<?> activatePassenger(@PathVariable("activationId") String id)
    {
    	try {
    		SuccessDTO dto = activationService.activatePassenger(id);
            return new ResponseEntity<SuccessDTO>(dto, HttpStatus.OK);
    	}
    	catch(ObjectNotFoundException e) {
    		ErrorDTO dto = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(dto, HttpStatus.NOT_FOUND);
    	}
    	catch(ActionExpiredException e) {
    		ErrorDTO dto = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(dto, HttpStatus.BAD_REQUEST);
    	}
    	
    }
    @GetMapping("/activate/resend/{activationId}")
    public ResponseEntity<?> activatePassengerResend(@PathVariable("activationId") String id)
    {
    	try {
    		SuccessDTO dto = userService.resendActivation(id);
            return new ResponseEntity<SuccessDTO>(dto, HttpStatus.OK);
    	}
    	catch(ObjectNotFoundException e) {
    		ErrorDTO dto = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(dto, HttpStatus.NOT_FOUND);
    	}    	
    }    
    
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("")
	public ResponseEntity<?> createRoot(Principal principal) {
		return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
	}

}