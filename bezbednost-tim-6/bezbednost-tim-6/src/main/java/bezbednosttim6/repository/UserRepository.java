package bezbednosttim6.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bezbednosttim6.model.Role;
import bezbednosttim6.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

	void deleteUserById(Long id);

	Optional<User> findUserById(Long id);
	
	User findUserByEmail(String email);
	Optional<User> findByIdAndRole(Long id, Role role);

	Optional<List<User>> findAllByRole(Role role);
	
	Page<User> findAllByRole(Role role, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% OR u.name LIKE %:keyword% OR u.surname LIKE %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);

	@Query("SELECT u.email FROM User u")
	List<String> getAllMail();
	
	@Query("SELECT u FROM User u WHERE u.role != :role")
    List<User> findAllUsersNotRole(@Param("role") Role role);
	
	@Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllUsersByRole(@Param("role") Role role);
	
	@Query("SELECT u FROM User u WHERE u.role = :role AND (u.name like %:keyword% OR u.surname like %:keyword% OR CONCAT(u.name,' ', u.surname) like %:keyword%)")
    List<User> searchByKeywordAndRole(@Param("keyword") String keyword, @Param("role") Role role);
	
	@Query("SELECT u FROM User u WHERE u.role != :role AND (u.name like %:keyword% OR u.surname like %:keyword% OR CONCAT(u.name,' ', u.surname) like %:keyword%)")
    List<User> searchByKeywordAndNotRole(@Param("keyword") String keyword, @Param("role") Role role);

}
