package media.fynema.api.services;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.CreateMediaSeverRequestDTO;
import media.fynema.api.model.MediaServer;
import media.fynema.api.repository.MediaServerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServerService {
    private final MediaServerRepository mediaServerRepository;

    public MediaServer create(CreateMediaSeverRequestDTO mediaServer) {
        MediaServer existingServer = mediaServerRepository.findByHost(mediaServer.host());
        if (existingServer != null) {
            return existingServer;
        }

        MediaServer newMediaServer = new MediaServer();
        newMediaServer.setHost(mediaServer.host());
        newMediaServer.setType(mediaServer.type());
        mediaServerRepository.save(newMediaServer);
        return newMediaServer;
    }

    public List<MediaServer> getAll() {
        return mediaServerRepository.findAll();
    }

    public MediaServer deleteOne(@RequestParam Integer id) {
        MediaServer server = mediaServerRepository.findById(id).orElse(null);
        if (server != null) {
            mediaServerRepository.delete(server);
            return server;
        } else {
            return null;
        }
    }

}
