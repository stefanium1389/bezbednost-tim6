package bezbednosttim6.repository;

import bezbednosttim6.model.Activation;
import bezbednosttim6.model.TwoFactorAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TwoFactorRepository extends JpaRepository<TwoFactorAuth, Long>{
	Optional<TwoFactorAuth> findAuthByToken(String token);

}
