package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.CreateMediaSeverRequestDTO;
import media.fynema.api.model.MediaServer;
import media.fynema.api.repository.MediaServerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/mediaserver")
@RequiredArgsConstructor
class MediaServerController {
    private final MediaServerRepository mediaServerRepository;

    @PostMapping
    public ResponseEntity<MediaServer> create(@RequestBody CreateMediaSeverRequestDTO mediaServer) {
        MediaServer existingServer = mediaServerRepository.findByHost(mediaServer.host());
        if (existingServer != null) {
            return ResponseEntity.ok(existingServer);
        }

        MediaServer newMediaServer = new MediaServer();
        newMediaServer.setHost(mediaServer.host());
        newMediaServer.setType(mediaServer.type());
        mediaServerRepository.save(newMediaServer);
        return ResponseEntity.ok(newMediaServer);
    }
}
