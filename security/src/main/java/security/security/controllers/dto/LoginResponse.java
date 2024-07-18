package security.security.controllers.dto;

public record LoginResponse (String acessToken, Long expiring) {

}
