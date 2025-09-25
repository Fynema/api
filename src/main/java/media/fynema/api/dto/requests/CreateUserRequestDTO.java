package media.fynema.api.dto.requests;

public record CreateUserRequestDTO(
        String username,
        String password
) {
}
