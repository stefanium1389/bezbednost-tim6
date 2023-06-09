package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.exception.TypeNotFoundException;
import bezbednosttim6.model.Certificate;
import bezbednosttim6.model.CertificateRequest;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.User;
import bezbednosttim6.security.LogIdUtil;
import bezbednosttim6.service.CertificateRequestService;
import bezbednosttim6.service.CertificateService;
import bezbednosttim6.service.CertificateValidationService;
import bezbednosttim6.service.DownloadFileService;
import bezbednosttim6.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.operator.OperatorCreationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.x500.X500Principal;

import java.io.ByteArrayInputStream;
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
	
	@Autowired
	private DownloadFileService downloadService;

	private static final Logger logger = LogManager.getLogger(CertificateController.class);
	private LogIdUtil util = new LogIdUtil();


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
			util.getNewLogId();
			logger.error("Error occurred while requesting a certificate: " + e.getMessage());
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
			util.getNewLogId();
			logger.error("Error occurred while viewing sent requests: " + e.getMessage());
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
			util.getNewLogId();
			logger.error("Error occurred while viewing received: " + e.getMessage());
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("request/view")
	public ResponseEntity<?> getAllCertificateRequests() {
		util.getNewLogId();
		logger.info("Admin requested all certificate requests");
		util.getNewLogId();
		logger.info("Returned successfully");
		return new ResponseEntity<>(this.certificateRequestService.getAll(), HttpStatus.OK);
	}

	
	@GetMapping("getAll")
	public ResponseEntity<?> getAllCertificates() {
		util.getNewLogId();
		logger.info("All certificates requested");
		util.getNewLogId();
		logger.info("Returned successfully");
		return new ResponseEntity<>(this.certificateService.getAllCertificateDTOs(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("isValid/{serialNumber}")
	public ResponseEntity<?> checkIsValid(@PathVariable("serialNumber") Long serialNumber){
		
		try {
			this.certificateValidationService.isValidFromSerial(serialNumber);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(ObjectNotFoundException e) {
			util.getNewLogId();
			logger.error("Error occurred checking certificate validity: " + e.getMessage());
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			util.getNewLogId();
			logger.error("Error occurred checking certificate validity: " + e.getMessage());
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PostMapping("isValidFile")
	public ResponseEntity<?> checkIsValidFile(@RequestParam("file") MultipartFile file){
				
		try {
			this.certificateValidationService.isValidFromFile(file);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(ObjectNotFoundException e) {
			util.getNewLogId();
			logger.error("Error occurred checking certificate validity: " + e.getMessage());
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			util.getNewLogId();
			logger.error("Error occurred checking certificate validity: " + e.getMessage());
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PutMapping ("request/reject/{requestId}")
	public ResponseEntity<?> rejectRequest (@PathVariable("requestId") Long requestId, @RequestBody ReasonDTO reasonDTO, Principal principal) {
		try {
			CertificateRequest newRequest = certificateRequestService.rejectRequest(requestId, reasonDTO.getReason(), principal);
			return new ResponseEntity<>(new CertificateRequestResponseDTO(newRequest), HttpStatus.OK);
		} catch (Exception e)
		{
			util.getNewLogId();
			logger.error("Error occurred while rejecting request " + requestId.toString() + " : " + e.getMessage());
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PutMapping ("request/accept/{requestId}")
	public ResponseEntity<?> acceptRequest (@PathVariable("requestId") Long requestId, Principal principal) {
		try {
			CertificateRequest newRequest = certificateRequestService.acceptRequest(requestId, principal);
			return new ResponseEntity<>(new CertificateRequestResponseDTO(newRequest), HttpStatus.OK);
		} catch (Exception e)
		{
			util.getNewLogId();
			logger.error("Error occurred while accepting request " + requestId.toString() + " : " + e.getMessage());
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.NOT_FOUND);
		}
	}
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping ("download/{serialNumber}")
	public ResponseEntity<?> downloadCertificate (@PathVariable("serialNumber") Long serialNumber, Principal principal) {
		util.getNewLogId();
		logger.info("User requested a certificate " + serialNumber.toString() + " as a file");
		try {
			byte[] zipBytes = downloadService.getZipBytes(serialNumber, principal);
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
	        headers.set(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("certificate.zip").build().toString());
			
	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(zipBytes);
		} catch (Exception e)
		{
			util.getNewLogId();
			logger.error("Error occurred while downloading certificate: " + e.getMessage());
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@PutMapping ("revoke/{serialNumber}")
	public ResponseEntity<?> revokeCertificate (@PathVariable("serialNumber") Long serialNumber, @RequestBody ReasonDTO reasonDTO, Principal principal) {
		try {
			certificateService.revokeCertificate(serialNumber, principal, reasonDTO.getReason());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e)
		{
			util.getNewLogId();
			logger.error("Error occurred while revoking certificate with serial number " + serialNumber.toString() + ": " + e.getMessage());
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("hello")
	public String hello() {
		util.getNewLogId();
		logger.info("bla");
		return "prosao";
	}

}