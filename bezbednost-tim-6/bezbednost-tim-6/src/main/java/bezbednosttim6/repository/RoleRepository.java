package bezbednosttim6.repository;

import bezbednosttim6.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByName(String name);
    Optional<Role> findById(Integer id);
}
