package bezbednosttim6.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bezbednosttim6.model.Certificate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long>  {

    @Query(value = "select * from certificates where serial_number = ?1", nativeQuery = true)
    public Optional<Certificate> findBySerialNumber(Long serialNumber);
}
