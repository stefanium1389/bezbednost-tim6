package bezbednosttim6.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bezbednosttim6.model.Activation;

public interface ActivationRepository extends JpaRepository<Activation, Long>{

	Optional<Activation> findActivationByToken(String token);

}
