package media.fynema.api.dto.responses;

public record CreateTokensResponseDTO(
        String accessToken,
        String refreshToken
) {
}