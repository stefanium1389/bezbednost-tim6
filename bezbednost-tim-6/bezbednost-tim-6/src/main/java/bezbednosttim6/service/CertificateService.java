package bezbednosttim6.service;


import bezbednosttim6.controller.CertificateController;
import bezbednosttim6.dto.*;
import bezbednosttim6.exception.*;
import bezbednosttim6.model.*;
import bezbednosttim6.model.Certificate;
import bezbednosttim6.model.CertificateRequest;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.RequestStatus;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.CertificateRepository;
import bezbednosttim6.repository.CertificateRequestRepository;
import bezbednosttim6.repository.UserRepository;
import bezbednosttim6.security.LogIdUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.security.auth.x500.X500Principal;

import static org.aspectj.runtime.internal.Conversions.longValue;


@Service
public class CertificateService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CertificateRepository certificateRepo;

	@Autowired
	private CertificateRequestRepository certificateRequestRepo;

	private static final Logger logger = LogManager.getLogger(CertificateService.class);
	private LogIdUtil util = new LogIdUtil();

//	private X509Certificate root;



	public Optional<Certificate> getBySerialNumber(Long serialNumber) {
		return certificateRepo.findBySerialNumber(serialNumber);
	}


	/*
	public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData) {
		try {
			// Posto klasa za generisanje sertifikata ne moze da primi direktno privatni kljuc pravi se builder za objekat
			// Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
			// Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifikata
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

			// Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
			builder = builder.setProvider("BC");

			// Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
			ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

			// Postavljaju se podaci za generisanje sertifikata
			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
					issuerData.getX500name(),
					new BigInteger(subjectData.getSerialNumber()),
					subjectData.getStartDate(),
					subjectData.getEndDate(),
					subjectData.getX500name(),
					subjectData.getPublicKey());

			// Generise se sertifikat
			X509CertificateHolder certHolder = certGen.build(contentSigner);

			// Builder generise sertifikat kao objekat klase X509CertificateHolder
			// Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");

			// Konvertuje objekat u sertifikat
			return certConverter.getCertificate(certHolder);
		} catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException e) {
			e.printStackTrace();
		}
		return null;
	}
	 */

	// root
	private X509Certificate generateCA(KeyPair keyPair,
									String hashAlgorithm,
									String cn,
									long days)
			throws OperatorCreationException, CertificateException, CertIOException
	{
		Instant now = Instant.now();
		Date validFrom = Date.from(now);
		Date validTo = Date.from(now.plus(Duration.ofDays(days)));

		ContentSigner contentSigner = new JcaContentSignerBuilder(hashAlgorithm).build(keyPair.getPrivate());
		X500Name x500Name = new X500Name("CN=" + cn);
		X509v3CertificateBuilder certificateBuilder =
				new JcaX509v3CertificateBuilder(x500Name,
						BigInteger.valueOf(now.toEpochMilli()),
						validFrom,
						validTo,
						x500Name,
						keyPair.getPublic())
						.addExtension(Extension.subjectKeyIdentifier, false, createSubjectKeyId(keyPair.getPublic()))
						.addExtension(Extension.authorityKeyIdentifier, false, createAuthorityKeyId(keyPair.getPublic()))
						.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));

		return new JcaX509CertificateConverter()
				.setProvider(new BouncyCastleProvider()).getCertificate(certificateBuilder.build(contentSigner));
	}


	private X509Certificate generateIntermediateEnd(
			KeyPair subjectKP,
			String  subjectCN,
			KeyPair issuerKP,
			String  issuerCN,
			String hashAlgorithm,
			long days)
			throws GeneralSecurityException, IOException, OperatorCreationException {

		PublicKey subjectPub = subjectKP.getPublic();
		PrivateKey issuerPriv = issuerKP.getPrivate();
		PublicKey issuerPub = issuerKP.getPublic();

		Instant now = Instant.now();
		Date validFrom = Date.from(now);
		Date validTo = Date.from(now.plus(Duration.ofDays(days)));

		ContentSigner contentSigner = new JcaContentSignerBuilder(hashAlgorithm).build(issuerPriv);
		X509v3CertificateBuilder certificateBuilder
				= new JcaX509v3CertificateBuilder(new X500Name("CN=" + issuerCN),
				BigInteger.valueOf(now.toEpochMilli()),
				validFrom,
				validTo,
				new X500Name("CN=" + subjectCN),
				subjectPub)
				.addExtension(Extension.subjectKeyIdentifier,false, createSubjectKeyId(subjectPub))
				.addExtension(Extension.authorityKeyIdentifier,false, createAuthorityKeyId(issuerPub));
		return new JcaX509CertificateConverter()
				.setProvider(new BouncyCastleProvider()).getCertificate(certificateBuilder.build(contentSigner));
		}


	/**
	 * Creates the hash value of the public key.
	 *
	 * @param publicKey of the certificate
	 *
	 * @return SubjectKeyIdentifier hash
	 *
	 * @throws OperatorCreationException
	 */
	private static SubjectKeyIdentifier createSubjectKeyId(final PublicKey publicKey) throws OperatorCreationException {
		final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
		final DigestCalculator digCalc =
				new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

		return new X509ExtensionUtils(digCalc).createSubjectKeyIdentifier(publicKeyInfo);
	}

	/**
	 * Creates the hash value of the authority public key.
	 *
	 * @param publicKey of the authority certificate
	 *
	 * @return AuthorityKeyIdentifier hash
	 *
	 * @throws OperatorCreationException
	 */
	private static AuthorityKeyIdentifier createAuthorityKeyId(final PublicKey publicKey)
			throws OperatorCreationException
	{
		final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
		final DigestCalculator digCalc =
				new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

		return new X509ExtensionUtils(digCalc).createAuthorityKeyIdentifier(publicKeyInfo);
	}

	private void exportCertificate(X509Certificate certificate, PrivateKey privateKey, String path, CertificateType type, User user, Long issuerId) throws IOException, CertificateEncodingException {
		if (issuerId == null){
			long longValue = certificate.getSerialNumber().longValue();
			issuerId = Long.valueOf(longValue);
		}
		// Store Certificate
		FileOutputStream fos;
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(certificate.getEncoded());
		File file = new File(path + "public/" + certificate.getSerialNumber() + ".cer");
		file.createNewFile();
		fos = new FileOutputStream(file);
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();

		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		file = new File(path + "private/" + certificate.getSerialNumber() + ".key");
		file.createNewFile();
		fos = new FileOutputStream(file);
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();

		X500Principal principal = certificate.getSubjectX500Principal();
		X500Name x500name = new X500Name(principal.getName());
		RDN cn = x500name.getRDNs(BCStyle.CN)[0];
		String commonName = IETFUtils.valueToString(cn.getFirst().getValue());

		// MySql
		Certificate certificate1 = new Certificate(longValue(certificate.getSerialNumber()), certificate.getSigAlgName(), issuerId,
				certificate.getNotBefore(), certificate.getNotAfter(), CertificateStatus.VALID, type, CertificateRevocationStatus.GOOD,
				user, commonName, null);
		certificateRepo.save(certificate1);

	}

	private KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CertificateDTO> getAllCertificateDTOs() {
		List<Certificate> lista = certificateRepo.findAll();
		List<CertificateDTO> dtos = new ArrayList<CertificateDTO>();
		for(Certificate c : lista) {
			dtos.add(new CertificateDTO(c));
		}
		return dtos;
	}

	public void createRoot(User user, String commonName, Duration duration) throws CertificateException, OperatorCreationException, IOException {
		util.getNewLogId();
		logger.info("Create root certificate with commonName: " + commonName + " for user: " + user.getId().toString());
		KeyPair keyPair = generateKeyPair();
		long days = duration.toDays();
		X509Certificate certificate = generateCA(keyPair, "SHA256WithRSAEncryption", commonName, days);
//		this.root = certificate;
		exportCertificate(certificate, keyPair.getPrivate(), "src/main/resources/certificates/", CertificateType.ROOT, user, null);
		util.getNewLogId();
		logger.info("Root certificate successfully created");
	}

	public void createIntermediate(User user, Long serialNumber, String commonName, Duration duration) throws GeneralSecurityException, OperatorCreationException, IOException {
		util.getNewLogId();
		logger.info("Create intermediate certificate with commonName: " + commonName + " for user: " + user.getId().toString());
		Optional<Certificate> certificateOpt = getBySerialNumber(serialNumber);

		if(certificateOpt.isEmpty()) {
			throw new CertificateNotFoundException("Certificate with serial number " + serialNumber + " not found");
		}

		Certificate fromCertificate = certificateOpt.get();

		KeyPair subjectKP = generateKeyPair();

		byte[] key = Files.readAllBytes(Paths.get("src/main/resources/certificates/private/" + serialNumber + ".key"));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		byte[] certData = Files.readAllBytes(Paths.get("src/main/resources/certificates/public/" + serialNumber + ".cer"));
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		java.security.cert.Certificate cert = certFactory.generateCertificate(new ByteArrayInputStream(certData));

		// Extract the public key from the certificate
		PublicKey publicKey = cert.getPublicKey();

		KeyPair issuerKP = new KeyPair(publicKey, privateKey);

//		X500Principal principal = this.root.getSubjectX500Principal();
//		X500Name x500name = new X500Name(principal.getName());
//		RDN cn = x500name.getRDNs(BCStyle.CN)[0];
//		String issuerCN = IETFUtils.valueToString(cn.getFirst().getValue());

		X509Certificate certificate = generateIntermediateEnd(subjectKP, commonName, issuerKP, fromCertificate.getCommonName(), "SHA256WithRSAEncryption", duration.toDays());
		exportCertificate(certificate, subjectKP.getPrivate(), "src/main/resources/certificates/", CertificateType.INTERMEDIATE, user, fromCertificate.getSerialNumber());
		util.getNewLogId();
		logger.info("Intermediate certificate successfully created");
	}

	public void createEnd(User user, Long serialNumber, String commonName, Duration duration) throws GeneralSecurityException, OperatorCreationException, IOException {
		util.getNewLogId();
		logger.info("Create end certificate with commonName: " + commonName + " for user: " + user.getId().toString());
		Optional<Certificate> certificateOpt = getBySerialNumber(serialNumber);

		if(certificateOpt.isEmpty()) {
			throw new CertificateNotFoundException("Certificate with serial number " + serialNumber + " not found");
		}

		Certificate fromCertificate = certificateOpt.get();

		KeyPair subjectKP = generateKeyPair();

		byte[] key = Files.readAllBytes(Paths.get("src/main/resources/certificates/private/" + serialNumber + ".key"));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		byte[] certData = Files.readAllBytes(Paths.get("src/main/resources/certificates/public/" + serialNumber + ".cer"));
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		java.security.cert.Certificate cert = certFactory.generateCertificate(new ByteArrayInputStream(certData));

		// Extract the public key from the certificate
		PublicKey publicKey = cert.getPublicKey();

		KeyPair issuerKP = new KeyPair(publicKey, privateKey);

//		X500Principal principal = this.root.getSubjectX500Principal();
//		X500Name x500name = new X500Name(principal.getName());
//		RDN cn = x500name.getRDNs(BCStyle.CN)[0];
//		String issuerCN = IETFUtils.valueToString(cn.getFirst().getValue());

		X509Certificate certificate = generateIntermediateEnd(subjectKP, commonName, issuerKP, fromCertificate.getCommonName(), "SHA256WithRSAEncryption", 750);
		exportCertificate(certificate, subjectKP.getPrivate(), "src/main/resources/certificates/", CertificateType.END, user, fromCertificate.getSerialNumber());
		util.getNewLogId();
		logger.info("End certificate successfully created");
	}


	public void revokeCertificate(Long serialNumber, Principal principal, String reason) {
		User user = userRepo.findUserByEmail(principal.getName());
		util.getNewLogId();
		logger.warn("User with id " + user.getId().toString() +" wants to revoke certificate with serial number " + serialNumber.toString());
		boolean isAdmin = user.getRole().getId() == 1;
		Optional<Certificate> db_certificateOpt = certificateRepo.findBySerialNumber(serialNumber);

		if(db_certificateOpt.isEmpty()) {
			throw new ObjectNotFoundException("Certificate "+serialNumber+" not found!");
		}
		Certificate db_certificate = db_certificateOpt.get();

		if (!isAdmin && user.getId() != db_certificate.getUser().getId()) {
			throw new ConditionNotMetException("You are not the owner of this certificate and therefore cannot revoke it");
		}

		if (!isAdmin && db_certificate.getCertificateType() == CertificateType.ROOT) {
			throw new ConditionNotMetException("You don't have the permission to revoke this certificate");
		}

		if (db_certificate.getCertificateRevocationStatus() == CertificateRevocationStatus.REVOKED) {
			throw new ConditionNotMetException("Already revocated");
		}

		// rekurzija koja nadam se radi
		revokeCertificate(db_certificate, reason);
	}

	private void revokeCertificate(Certificate certificate, String reason) {
		util.getNewLogId();
		logger.warn("Revoking certificate with serial number " + certificate.getSerialNumber().toString());
		if (certificate.getCertificateType()!=CertificateType.END) {
			List<Certificate> children = certificateRepo.findByIssuer(certificate.getSerialNumber());
			for (Certificate child : children) {
				// nesto mi je ovde sumnjivo
				if (certificate.getSerialNumber().equals(child.getSerialNumber())) {
					continue;
				}
				revokeCertificate(child, reason);
			}
		}
		certificate.setRevocationReason(reason);
		certificate.setCertificateRevocationStatus(CertificateRevocationStatus.REVOKED);
		certificate.setStatus(CertificateStatus.NOTVALID);
		certificateRepo.save(certificate);
		certificateRepo.flush();
		util.getNewLogId();
		logger.warn("Successfully revoked certificate with serial number " + certificate.getSerialNumber().toString());
	}

}
