package media.fynema.api.repository;

import media.fynema.api.model.MediaServerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaServerCategoryRepository extends JpaRepository<MediaServerCategory, Integer> {

    Optional<MediaServerCategory> findByName(String name);
}