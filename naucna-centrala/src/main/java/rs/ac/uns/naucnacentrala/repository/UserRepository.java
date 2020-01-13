package rs.ac.uns.naucnacentrala.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.naucnacentrala.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value="SELECT u FROM User u WHERE u.username=?1")
	User findByUsername(String username);

	
}
