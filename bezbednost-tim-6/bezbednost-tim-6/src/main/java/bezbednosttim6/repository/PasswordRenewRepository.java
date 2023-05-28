package bezbednosttim6.repository;

import bezbednosttim6.model.PasswordRenew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PasswordRenewRepository extends JpaRepository<PasswordRenew, Long> {
    Optional<PasswordRenew> findByToken(String token);

    @Query(value = "SELECT * FROM password_renew WHERE email=?1 and successful = true and timestamp > (current_timestamp() - interval 30 day) ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    Optional<PasswordRenew> findByLatestTimestamp(String email);

    @Query(value = "SELECT old_password FROM password_renew WHERE email=?1 and successful = true", nativeQuery = true)
    List<String> findOldPasswords(String email);
}
