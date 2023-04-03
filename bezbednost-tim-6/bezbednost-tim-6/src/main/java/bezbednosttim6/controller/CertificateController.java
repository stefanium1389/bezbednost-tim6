package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.exception.TypeNotFoundException;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.User;
import bezbednosttim6.security.TokenUtils;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.TreeSet;


@RestController
@RequestMapping("api/cert/")
public class CertificateController {

	@Autowired
	private UserService userService;

	@Autowired
	private CertificateService certificateService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtils jwtTokenUtil;

	@Autowired
	PasswordEncoder passwordEncoder;

	private X509Certificate root;


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
		//TODO: napisati ovo lepše xd
		catch (Exception e)
		{
			ErrorDTO error = new ErrorDTO(e.getMessage());
			return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping ("request/view")
	public ResponseEntity<?> createRequest (Principal principal)
	{
		try
		{
			DTOList<CertificateRequestResponseDTO> response = certificateService.getAllForUser(principal.getName());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		//TODO: napisati ovo lepše xd
		catch (Exception e)
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


	@GetMapping("root")
	public ResponseEntity<?> createRoot(Principal principal) throws CertificateException, OperatorCreationException, IOException {
		KeyPair keyPair = certificateService.generateKeyPair();
		X509Certificate certificate = certificateService.generateCA(keyPair, "SHA256WithRSAEncryption", "Kris?", 1500);
		this.root = certificate;
		User user = userService.findUserByEmail(principal.getName());
		certificateService.exportCertificate(certificate, keyPair.getPrivate(), "src/main/resources/certificates/", CertificateType.ROOT, user);
		return new ResponseEntity<>("jej",HttpStatus.OK);
	}

	@GetMapping("intermediate")
	public ResponseEntity<?> createIntermediate(Principal principalUser) throws GeneralSecurityException, OperatorCreationException, IOException {
		User user = userService.findUserByEmail(principalUser.getName());
		KeyPair subjectKP = certificateService.generateKeyPair();
		String subjectCN = "lalala";
		BigInteger serNum = this.root.getSerialNumber();
		PublicKey publicKey = this.root.getPublicKey();

		byte[] key = Files.readAllBytes(Paths.get("src/main/resources/certificates/private/" + serNum + ".key"));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		KeyPair issuerKP = new KeyPair(publicKey, privateKey);

		X500Principal principal = this.root.getSubjectX500Principal();
		X500Name x500name = new X500Name(principal.getName());
		RDN cn = x500name.getRDNs(BCStyle.CN)[0];
		String issuerCN = IETFUtils.valueToString(cn.getFirst().getValue());

		X509Certificate certificate = certificateService.generateIntermediateEnd(subjectKP, subjectCN, issuerKP, issuerCN, "SHA256WithRSAEncryption", 750);
		certificateService.exportCertificate(certificate, subjectKP.getPrivate(), "src/main/resources/certificates/", CertificateType.INTERMEDIATE, user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("end")
	public ResponseEntity<?> createEnd(Principal principalUser) throws GeneralSecurityException, OperatorCreationException, IOException {
		User user = userService.findUserByEmail(principalUser.getName());
		KeyPair subjectKP = certificateService.generateKeyPair();
		String subjectCN = "lalala";
		BigInteger serNum = this.root.getSerialNumber();
		PublicKey publicKey = this.root.getPublicKey();

		byte[] key = Files.readAllBytes(Paths.get("src/main/resources/certificates/private/" + serNum + ".key"));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		KeyPair issuerKP = new KeyPair(publicKey, privateKey);

		X500Principal principal = this.root.getSubjectX500Principal();
		X500Name x500name = new X500Name(principal.getName());
		RDN cn = x500name.getRDNs(BCStyle.CN)[0];
		String issuerCN = IETFUtils.valueToString(cn.getFirst().getValue());

		X509Certificate certificate = certificateService.generateIntermediateEnd(subjectKP, subjectCN, issuerKP, issuerCN, "SHA256WithRSAEncryption", 750);
		certificateService.exportCertificate(certificate, subjectKP.getPrivate(), "src/main/resources/certificates/", CertificateType.END, user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

//	@GetMapping
//	public void lalala() {
//		TreeSet<String> algorithms = new TreeSet<>();
//		for (Provider provider : Security.getProviders())
//			for (Provider.Service service : provider.getServices())
//				if (service.getType().equals("Signature"))
//					algorithms.add(service.getAlgorithm());
//		for (String algorithm : algorithms)
//			System.out.println(algorithm);
//	}
}