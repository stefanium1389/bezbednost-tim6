package bezbednosttim6.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.repository.CertificateRepository;

@Service
public class CertificateValidationService {
	
	@Autowired
	private CertificateRepository repo;

	public void isValid(Long serialNumber) throws CertificateExpiredException, CertificateNotYetValidException {
		
		X509Certificate certificate = null;
		try {
			certificate = getCertificate(serialNumber);
		} catch (Exception e) {
			throw new ObjectNotFoundException("Certificate not found!");
		}
		try {
			certificate.checkValidity();
		} catch (CertificateExpiredException e) {
			//e.printStackTrace();
			throw new CertificateExpiredException(e.getMessage());
		} catch (CertificateNotYetValidException e) {
			//e.printStackTrace();
			throw new CertificateNotYetValidException(e.getMessage());
		}
	}

	private X509Certificate getCertificate(Long serialNumber) throws Exception { //za dobavljanje pravih sertifikata, fajlova valjda
		InputStream inStream = new FileInputStream("path/to/certificate.crt");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
        inStream.close();
        return cert;
		
	}

}
