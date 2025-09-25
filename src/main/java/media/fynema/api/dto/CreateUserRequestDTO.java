package media.fynema.api.dto;

public record CreateUserRequestDTO(
        String username,
        String password
) {
}
