package bezbednosttim6.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bezbednosttim6.exception.InvalidCertificateException;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.model.Certificate;
import bezbednosttim6.model.CertificateStatus;
import bezbednosttim6.repository.CertificateRepository;

@Service
public class CertificateValidationService {
	
	@Autowired
	private CertificateRepository repo;

	public void isValid(Long serialNumber) throws Exception {
		
		X509Certificate certificate = null;
		X509Certificate parent = null;
		
		Optional<Certificate> db_certificateOpt = repo.findBySerialNumber(serialNumber);
		
		if(db_certificateOpt.isEmpty()) {
			throw new ObjectNotFoundException("Certificate "+serialNumber+" not found!");
		}
		
		Certificate db_certificate = db_certificateOpt.get();
		
		if(db_certificate.getStatus() == CertificateStatus.NOTVALID) {
			throw new InvalidCertificateException("Certificate "+serialNumber+" not valid!");
		}
		
		try {
			certificate = getCertificate(serialNumber);
		} catch (Exception e) {
			throw new ObjectNotFoundException("Certificate "+serialNumber+" not found!");
		}
		
		try {
			certificate.checkValidity(); //proverava datume
			if(db_certificate.getSerialNumber() != db_certificate.getIssuer()) { //ako nije root
				parent = getCertificate(db_certificate.getIssuer());
				certificate.verify(parent.getPublicKey()); //verifikijue potpis sa kljucem roditelja
				try {
					isValid(db_certificate.getIssuer()); //verifikujemo i roditelja
				}
				catch (GeneralSecurityException e) { //ako je roditelj nevalidan, onda sam i ja
					db_certificate.setStatus(CertificateStatus.NOTVALID);
					repo.save(db_certificate);
					repo.flush();
					throw new GeneralSecurityException(e.getMessage());
				}
			}
			else { //ako jeste root
				certificate.verify(certificate.getPublicKey()); //sam sebe je potpisao, pa proverava sa svojim
			}
		} catch (GeneralSecurityException e) {
			//e.printStackTrace();
			db_certificate.setStatus(CertificateStatus.NOTVALID);
			repo.save(db_certificate);
			repo.flush();
			throw new GeneralSecurityException(e.getMessage());
		} 
	}

	private X509Certificate getCertificate(Long serialNumber) throws Exception {
		InputStream inStream = new FileInputStream("src/main/resources/certificates/public/" + serialNumber +".cer");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
        inStream.close();
        return cert;
		
	}

}
