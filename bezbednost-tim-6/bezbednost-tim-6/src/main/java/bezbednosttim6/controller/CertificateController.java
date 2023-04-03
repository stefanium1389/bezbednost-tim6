package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.exception.TypeNotFoundException;
import bezbednosttim6.security.TokenUtils;
import bezbednosttim6.service.CertificateService;
import bezbednosttim6.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("api/cert/")
public class CertificateController {

	@Autowired
	private CertificateService certificateService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@PostMapping ("request")
	public ResponseEntity<?> createRequest (@RequestBody CertificateRequestDTO certificateRequestDTO, Principal principal)
	{
		try
		{
			CertificateRequestResponseDTO response = certificateService.createRequest(certificateRequestDTO,principal.getName());
			return new ResponseEntity<CertificateRequestResponseDTO>(response,HttpStatus.OK);
		}
		catch(TypeNotFoundException e)
		{
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
		}
		//TODO: napisati ovo lepše xd
		catch (Exception e)
		{
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
		}
	}

}