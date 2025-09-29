package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.dto.requests.CreateMediaSeverCategoryRequestDTO;
import media.fynema.api.model.MediaServerCategory;
import media.fynema.api.services.MediaServerCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mediaserver/{mediaServerId}/category")
@RequiredArgsConstructor
class MediaServerCategoryController {
    private final MediaServerCategoryService mediaServerCategoryService;

    @PostMapping()
    public ResponseEntity<MediaServerCategory> create(
            @RequestBody CreateMediaSeverCategoryRequestDTO category,
            @PathVariable Integer mediaServerId
    ) {
        MediaServerCategory createdCategory = mediaServerCategoryService.create(category, mediaServerId);
        return ResponseEntity.ok(createdCategory);
    }

    @GetMapping()
    public ResponseEntity<List<MediaServerCategory>> getAll() {
        List<MediaServerCategory> servers = mediaServerCategoryService.getAll();
        return ResponseEntity.ok(servers);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<MediaServerCategory> deleteOne(
            @PathVariable Integer categoryId
    ) {
        MediaServerCategory deletedServer = mediaServerCategoryService.deleteOne(categoryId);
        if (deletedServer != null) {
            return ResponseEntity.ok(deletedServer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
