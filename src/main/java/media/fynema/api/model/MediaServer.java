package media.fynema.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import media.fynema.api.enums.MediaServerType;

@Entity
@Getter
@Setter
public class MediaServer {
    @Id
    @GeneratedValue
    private Long id;

    private String host;

    @Enumerated(EnumType.STRING)
    private MediaServerType type;

    private String downloadPath;
}
