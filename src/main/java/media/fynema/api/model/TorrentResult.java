package media.fynema.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TorrentResult {
    private String title;
    private String link;
    private String size;
    private String tracker;
    private String seeders;
}
