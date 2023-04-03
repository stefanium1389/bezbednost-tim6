package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.exception.TypeNotFoundException;
import bezbednosttim6.security.TokenUtils;
import bezbednosttim6.service.CertificateService;
import bezbednosttim6.service.CertificateValidationService;
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
	private UserService userService;

	@Autowired
	private CertificateService certificateService;
	
	@Autowired
	private CertificateValidationService certificateValidationService;
	
	
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
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAllCertificates() {
		
		return new ResponseEntity<>(this.certificateService.getAllCertificateDTOs(), HttpStatus.OK);
		
	}
	
	@GetMapping("isValid/{serialNumber}")
	public ResponseEntity<?> checkIsValid(@PathVariable("serialNumber") Long serialNumber){
		
		try {
			this.certificateValidationService.isValid(serialNumber);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
				
	}

}