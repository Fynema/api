package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.CreateMediaSeverRequestDTO;
import media.fynema.api.model.MediaServer;
import media.fynema.api.repository.MediaServerRepository;
import media.fynema.api.services.MediaServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mediaserver")
@RequiredArgsConstructor
class MediaServerController {
    private final MediaServerService mediaServerService;

    @PostMapping()
    public ResponseEntity<MediaServer> create(@RequestBody CreateMediaSeverRequestDTO mediaServer) {
        MediaServer createdServer = mediaServerService.create(mediaServer);
        return ResponseEntity.ok(createdServer);
    }

    @GetMapping()
    public ResponseEntity<List<MediaServer>> getAll() {
        List<MediaServer> servers = mediaServerService.getAll();
        return ResponseEntity.ok(servers);
    }

    @DeleteMapping()
    public ResponseEntity<MediaServer> deleteOne(@RequestParam Integer id) {
        MediaServer deletedServer = mediaServerService.deleteOne(id);
        if (deletedServer != null) {
            return ResponseEntity.ok(deletedServer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
