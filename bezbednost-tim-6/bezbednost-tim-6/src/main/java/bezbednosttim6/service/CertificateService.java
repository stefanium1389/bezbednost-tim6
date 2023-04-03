package bezbednosttim6.service;


import bezbednosttim6.dto.CertificateRequestDTO;
import bezbednosttim6.dto.CertificateRequestResponseDTO;
import bezbednosttim6.dto.LoginResponseDTO;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.exception.TypeNotFoundException;
import bezbednosttim6.model.*;
import bezbednosttim6.model.Certificate;
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
	private CertificateRequestRepository certificateRequestRepo;

	@Autowired
	private CertificateRepository certificateRepository;
	
	public User findUserById (Long id) 
	{
		return userRepo.findUserById(id).orElseThrow(()-> new ObjectNotFoundException("User not found."));
	}
	
	public User findUserByEmail(String email) {
		return userRepo.findUserByEmail(email);
	}


	public CertificateRequestResponseDTO createRequest(CertificateRequestDTO certificateRequestDTO, String mail) {

		String requestedType = certificateRequestDTO.getCertificateType().toUpperCase().trim();
		CertificateType type = null;
		if (requestedType.equals("ROOT"))
			type = CertificateType.ROOT;
		else if (requestedType.equals("INTERMEDIATE"))
			type = CertificateType.INTERMEDIATE;
		else if (requestedType.equals("END"))
			type = CertificateType.END;
		else throw new TypeNotFoundException("Type of certificate is not valid.");

		LocalDateTime now = LocalDateTime.now();
		//TODO: proveriti da li postoji id issuera
		//TODO: dodati koliko dugo treba da traje novi cert
		//nisam siguran u vezi ovog mail xd
		CertificateRequest newRequest = new CertificateRequest(type, certificateRequestDTO.getIssuerCertificateId(), mail, RequestStatus.CREATED, now);
		certificateRequestRepo.save(newRequest);

		return new CertificateRequestResponseDTO(certificateRequestDTO.getCertificateType(), certificateRequestDTO.getIssuerCertificateId(),mail,now);
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
		Certificate certificate1 = new Certificate(longValue(certificate.getSerialNumber()), certificate.getSigAlgName(), commonName,
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
}
