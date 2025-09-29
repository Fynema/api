package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.CreateMediaSeverRequestDTO;
import media.fynema.api.model.MediaServer;
import media.fynema.api.repository.MediaServerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<MediaServer>> getAll() {
        List<MediaServer> servers = mediaServerRepository.findAll();
        return ResponseEntity.ok(servers);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteOne(@RequestParam Integer id) {
        MediaServer server = mediaServerRepository.findById(id).orElse(null);
        if (server != null) {
            mediaServerRepository.delete(server);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
