package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.services.TmdbService;
import media.fynema.api.services.TmdbService.TMDBResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media/tmdb")
@RequiredArgsConstructor
public class TmdbController {

    private final TmdbService tmdbService;

    @GetMapping("/movie/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public TMDBResponse searchMovies(@RequestParam String query) {
        return tmdbService.searchMovie(query);
    }
}
