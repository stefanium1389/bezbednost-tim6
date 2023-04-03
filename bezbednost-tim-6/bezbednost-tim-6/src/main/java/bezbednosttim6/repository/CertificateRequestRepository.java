package bezbednosttim6.repository;

import bezbednosttim6.model.CertificateRequest;
import bezbednosttim6.model.Role;
import bezbednosttim6.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, Long> {

}
