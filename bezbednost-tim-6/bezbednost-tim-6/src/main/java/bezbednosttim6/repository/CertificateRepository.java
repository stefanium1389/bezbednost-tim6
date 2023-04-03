package bezbednosttim6.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bezbednosttim6.model.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long>  {
	
}
