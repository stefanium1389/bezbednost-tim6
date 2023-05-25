package bezbednosttim6.repository;

import bezbednosttim6.model.CertificateRevocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import bezbednosttim6.model.Certificate;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long>  {

    @Query(value = "select * from certificates where serial_number = ?1", nativeQuery = true)
    Optional<Certificate> findBySerialNumber(Long serialNumber);

    List<Certificate> findByCertificateRevocationStatus(CertificateRevocationStatus certificateRevocationStatus);

    List<Certificate> findByIssuer(Long issuer);
}
