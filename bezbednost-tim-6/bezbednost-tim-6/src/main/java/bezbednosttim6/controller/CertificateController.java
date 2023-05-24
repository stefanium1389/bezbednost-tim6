package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.exception.TypeNotFoundException;
import bezbednosttim6.model.Certificate;
import bezbednosttim6.model.CertificateRequest;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.User;
import bezbednosttim6.service.CertificateRequestService;
import bezbednosttim6.service.CertificateService;
import bezbednosttim6.service.CertificateValidationService;
import bezbednosttim6.service.UserService;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.operator.OperatorCreationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Optional;


@RestController
@RequestMapping("api/cert/")
public class CertificateController {

	@Autowired
	private UserService userService;

	@Autowired
	private CertificateService certificateService;

	@Autowired
	PasswordEncoder passwordEncoder;

	private X509Certificate root;
	@Autowired
	private CertificateValidationService certificateValidationService;

	@Autowired
	private CertificateRequestService certificateRequestService;


	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PostMapping ("request")
	public ResponseEntity<?> createRequest (@RequestBody CertificateRequestDTO certificateRequestDTO, Principal principal)
	{
		try
		{
			CertificateRequestResponseDTO response = certificateRequestService.createRequest(certificateRequestDTO,principal);
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

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping ("request/sent/view")
	public ResponseEntity<?> viewSentRequests (Principal principal)
	{
		try
		{
			DTOList<CertificateRequestResponseDTO> response = certificateRequestService.getAllForUser(principal.getName());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		//TODO: napisati ovo lepše xd
		catch (Exception e)
		{
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping ("request/received/view")
	public ResponseEntity<?> viewReceivedRequests (Principal principal)
	{
		try
		{
			DTOList<CertificateRequestResponseDTO> response = certificateRequestService.getAllForIssuer(principal.getName());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		//TODO: napisati ovo lepše xd
		catch (Exception e)
		{
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("request/view")
	public ResponseEntity<?> getAllCertificateRequests() {
		return new ResponseEntity<>(this.certificateRequestService.getAll(), HttpStatus.OK);
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
		catch(ObjectNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("isValidFile")
	public ResponseEntity<?> checkIsValidFile(@RequestParam("file") MultipartFile file){
		
		System.err.println(file.getOriginalFilename());
		System.err.println(file.getSize());
		System.err.println(file.getContentType());
		return new ResponseEntity<>(HttpStatus.OK);
		
	}


	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PostMapping ("request/reject/{requestId}")
	public ResponseEntity<?> rejectRequest (@PathVariable("requestId") Long requestId, @RequestBody ReasonDTO reasonDTO, Principal principal) {
		try {
			CertificateRequest newRequest = certificateRequestService.rejectRequest(requestId, reasonDTO.getReason(), principal);
			return new ResponseEntity<>(new CertificateRequestResponseDTO(newRequest), HttpStatus.OK);
		} catch (Exception e)
		{
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PostMapping ("request/accept/{requestId}")
	public ResponseEntity<?> acceptRequest (@PathVariable("requestId") Long requestId, Principal principal) {
		try {
			CertificateRequest newRequest = certificateRequestService.acceptRequest(requestId, principal);
			return new ResponseEntity<>(new CertificateRequestResponseDTO(newRequest), HttpStatus.OK);
		} catch (Exception e)
		{
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.NOT_FOUND);
		}
	}

}