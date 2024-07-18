package security.security.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import security.security.controllers.dto.CreateUserDto;
import security.security.entities.Roles;
import security.security.entities.User;
import security.security.repositories.RolesRepository;
import security.security.repositories.UserRepository;

@RestController
public class UserController {
	
	private final UserRepository userRepository;
	private final RolesRepository rolesRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserController(UserRepository userRepository,
						  RolesRepository rolesRepository,
						  BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.rolesRepository = rolesRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	
	@Transactional
	@PostMapping("/users")
	public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto){
		
		var basicRole = rolesRepository.findByName(Roles.Values.basic.name());
		
		var userFromDb = userRepository.findByUsername(dto.username());
		
		if(userFromDb.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		var user = new User();
		user.setUsername(dto.username());
		user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
		user.setRoles(Set.of(basicRole));
		
		userRepository.save(user);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/users")
	@PreAuthorize("hasAuthority('SCOPE_admin')")
	public ResponseEntity<List<User>> listUsers(){
		var users = userRepository.findAll();
		
		return ResponseEntity.ok(users);
	}

}
