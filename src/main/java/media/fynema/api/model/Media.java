package media.fynema.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import media.fynema.api.enums.MediaStatus;

@Entity
@Getter
@Setter
public class Media {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private Long tmdb_id;

    private Long download_id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "media_server_id")
    private MediaServer mediaServer;

    @ManyToOne
    @JoinColumn(name = "quality_id")
    private Quality quality;

    @Enumerated(EnumType.STRING)
    private MediaStatus status;
}
