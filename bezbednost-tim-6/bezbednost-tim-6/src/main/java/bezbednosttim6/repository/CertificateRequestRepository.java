package bezbednosttim6.repository;

import bezbednosttim6.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, Long> {

    List<CertificateRequest> findAllByUserId(Long userId);

    List<CertificateRequest> findAllByIssuerIdAndStatus(Long issuerId, RequestStatus status);

    Optional<CertificateRequest> findById(Long id);

}
