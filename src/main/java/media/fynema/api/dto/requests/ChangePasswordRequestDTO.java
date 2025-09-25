package media.fynema.api.dto.requests;

public record ChangePasswordRequestDTO(
        String oldPassword,
        String newPassword
) {
}
