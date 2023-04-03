package bezbednosttim6.service;


import bezbednosttim6.dto.*;
import bezbednosttim6.exception.*;
import bezbednosttim6.model.*;
import bezbednosttim6.model.Certificate;
import bezbednosttim6.model.Certificate;
import bezbednosttim6.model.CertificateRequest;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.RequestStatus;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.CertificateRepository;
import bezbednosttim6.repository.CertificateRequestRepository;
import bezbednosttim6.repository.UserRepository;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
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

	@Autowired
	private CertificateRepository certificateRepository;

	public CertificateRequestResponseDTO createRequest(CertificateRequestDTO certificateRequestDTO, String mail) {

		User user = userRepo.findUserByEmail(mail);
		if (user == null)
		{
			throw new UserNotFoundException("User not found.");
		}

		String requestedType = certificateRequestDTO.getCertificateType().toUpperCase().trim();
		CertificateType type = findCertificateType(requestedType);

		if (type == CertificateType.ROOT && user.getRole().getName().equals("user"))
			throw new TypePermissionException("You don't have the permission to create root certificates");

		LocalDateTime now = LocalDateTime.now();
		Duration duration;
		try {
			// pogledajte (hoverujte) .parse metodu za primere
			duration = Duration.parse(certificateRequestDTO.getDuration());
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid duration provided");
		}

		if (!checkIfCertificateExist(certificateRequestDTO.getIssuerCertificateId()))
		{
			throw new CertificateNotFoundException("Issuer certificate not found");
		}

		if (!checkIfValidDuration(certificateRequestDTO.getIssuerCertificateId(),duration))
		{
			throw new InvalidArgumentException("Requested duration is longer than possible");
		}


		CertificateRequest newRequest = new CertificateRequest(type, certificateRequestDTO.getIssuerCertificateId(), user.getId(), RequestStatus.CREATED, now, duration);
		certificateRequestRepo.save(newRequest);

		return new CertificateRequestResponseDTO(certificateRequestDTO.getCertificateType(), certificateRequestDTO.getIssuerCertificateId(),user.getId(),now,RequestStatus.CREATED.toString());
	}

	private boolean checkIfValidDuration(Long issuerCertificateId, Duration requestedDuration) {
		//TODO: zapravo dobaviti certifikat, napraviti duration od now i enddate
		//"something must be done, 500 years" xdd
		Duration dummyDuration = Duration.of(500L * 365, ChronoUnit.DAYS);


		if (requestedDuration.compareTo(dummyDuration) > 0) {
			return false;
		}
		return true;
	}


	private boolean checkIfCertificateExist(Long issuerCertificateId) {
		//TODO: implementirati
		return true;
	}

	private CertificateType findCertificateType(String type)
	{
		if (type.equals("ROOT"))
			return CertificateType.ROOT;
		else if (type.equals("INTERMEDIATE"))
			return CertificateType.INTERMEDIATE;
		else if (type.equals("END"))
			return CertificateType.END;
		else throw new TypeNotFoundException("Type of certificate is not valid.");
	}


	public DTOList<CertificateRequestResponseDTO> getAllForUser(String mail) {

		User user = userRepo.findUserByEmail(mail);
		if (user == null)
		{
			throw new UserNotFoundException("User not found.");
		}
		DTOList<CertificateRequestResponseDTO> dtoList = new DTOList<>();
		List<CertificateRequest> requests = certificateRequestRepo.findAllByUserId(user.getId());
		for (CertificateRequest cr : requests)
		{
			CertificateRequestResponseDTO dto = new CertificateRequestResponseDTO(cr);
			dtoList.add(dto);
		}

		return dtoList;
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
	public X509Certificate generateCA(KeyPair keyPair,
									String hashAlgorithm,
									String cn,
									int days)
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


	public X509Certificate generateIntermediateEnd(
			KeyPair subjectKP,
			String  subjectCN,
			KeyPair issuerKP,
			String  issuerCN,
			String hashAlgorithm,
			int days)
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

	public void exportCertificate(X509Certificate certificate, PrivateKey privateKey, String path, CertificateType type, User user) throws IOException, CertificateEncodingException {
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
		Certificate certificate1 = new Certificate(longValue(certificate.getSerialNumber()), certificate.getSigAlgName(), 1L,
				certificate.getNotBefore(), certificate.getNotAfter(), CertificateStatus.VALID, type, user);
		certificateRepository.save(certificate1);

	}

	public KeyPair generateKeyPair() {
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


}
