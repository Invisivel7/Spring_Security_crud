package security.security.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import security.security.controllers.dto.LoginRequest;
import security.security.controllers.dto.LoginResponse;
import security.security.entities.Roles;
import security.security.repositories.UserRepository;

@RestController
public class TokenController {

	private final JwtEncoder jwtEncoder;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public TokenController(JwtEncoder jwtEncoder, 
							UserRepository userRepository,
							BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.jwtEncoder = jwtEncoder;
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest loginRequest){
		var user = userRepository.findByUsername(loginRequest.username());
		
		if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
			throw new BadCredentialsException("User ou password is invalid");
		}
		
		var now = Instant.now();
		var expiresin = 300L;
		
		var scopes = user.get().getRoles()
					.stream()
					.map(Roles::getName)
					.collect(Collectors.joining(" "));
		
		var claims = JwtClaimsSet.builder()
				.issuer("mybackend")
				.subject(user.get().getUser_id().toString())
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expiresin))
				.claim("scope", scopes)
				.build();
		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		return ResponseEntity.ok(new LoginResponse(jwtValue, expiresin));
	}
}
