package media.fynema.api.dto.requests;

import media.fynema.api.enums.MediaServerType;

public record CreateMediaSeverRequestDTO(
        String host,
        MediaServerType type
) {
}
