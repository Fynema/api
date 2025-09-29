package media.fynema.api.services;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.CreateMediaSeverCategoryRequestDTO;
import media.fynema.api.model.MediaServer;
import media.fynema.api.model.MediaServerCategory;
import media.fynema.api.repository.MediaServerCategoryRepository;
import media.fynema.api.repository.MediaServerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaServerCategoryService {
    private final MediaServerCategoryRepository mediaServerCategoryRepository;
    private final MediaServerRepository mediaServerRepository;

    public MediaServerCategory create(
            CreateMediaSeverCategoryRequestDTO category,
            Integer mediaServerId
    ) {
        MediaServer mediaServer = mediaServerRepository.findById(mediaServerId)
                .orElseThrow(() -> new IllegalArgumentException("Media server not found"));

        mediaServerCategoryRepository.findByName((category.name()))
                .ifPresent(existingCategory -> {
                    throw new IllegalArgumentException("Category with the same name already exists");
                });

        MediaServerCategory newMediaServerCategory = new MediaServerCategory();
        newMediaServerCategory.setName(category.name());
        newMediaServerCategory.setPath(category.path());
        newMediaServerCategory.setMediaServer(mediaServer);
        return mediaServerCategoryRepository.save(newMediaServerCategory);
    }

    public List<MediaServerCategory> getAll() {
        return mediaServerCategoryRepository.findAll();
    }

    public MediaServerCategory deleteOne(@RequestParam Integer id) {
        MediaServerCategory category = mediaServerCategoryRepository.findById(id).orElse(null);
        if (category != null) {
            mediaServerCategoryRepository.delete(category);
            return category;
        } else {
            return null;
        }
    }
}
