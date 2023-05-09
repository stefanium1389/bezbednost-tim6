package bezbednosttim6.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bezbednosttim6.model.PasswordReset;


public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

	Optional<PasswordReset> findByToken(String token);

}
