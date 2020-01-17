package rs.ac.uns.naucnacentrala.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.naucnacentrala.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value="SELECT u FROM User u WHERE u.username=?1")
	User findByUsername(String username);

	@Query(value = "SELECT u FROM User u JOIN u.authorities a WHERE a.name=?1")
	List<User> findAllByRole(String role);

	User findByEmail(String email);
	
}
