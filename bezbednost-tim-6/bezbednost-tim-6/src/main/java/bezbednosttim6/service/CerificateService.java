package bezbednosttim6.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bezbednosttim6.model.Certificate;
import bezbednosttim6.repository.CertificateRepository;

@Service
public class CerificateService {
	
	@Autowired
	private CertificateRepository certRepo;
	
	public List<Certificate> getAllCertificates() {
		
		return this.certRepo.findAll();
		
	}
	
	
	
}
