package media.fynema.api.repository;

import media.fynema.api.model.MediaServer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaServerRepository extends JpaRepository<MediaServer, Integer> {
    MediaServer findById(int id);

    MediaServer findByHost(String host);
}