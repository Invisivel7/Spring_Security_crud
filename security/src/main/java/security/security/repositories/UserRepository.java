package security.security.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import security.security.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	
	Optional<User> findByUsername(String username);
}
