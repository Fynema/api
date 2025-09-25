package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.model.TorrentResult;
import media.fynema.api.services.JackettService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
class MediaController {
    private final JackettService jackettService;

    @GetMapping("movie/search")
    public List<TorrentResult> searchMovie(@RequestParam String query, @RequestParam String year, @RequestParam(required = false) List<String> filters) {
        return jackettService.searchMovie(query, year, filters);
    }
}
