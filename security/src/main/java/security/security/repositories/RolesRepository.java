package security.security.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import security.security.entities.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long>{

	Roles findByName(String name);
}
