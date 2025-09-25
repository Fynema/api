package media.fynema.api.controller;

import lombok.RequiredArgsConstructor;
import media.fynema.api.services.Aria2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media/download")
@RequiredArgsConstructor
public class DownloadController {
    private final Aria2Service aria2Service;

    @PostMapping("/movie")
    public ResponseEntity<String> downloadMovie(
            @RequestParam String torrentUrl
    ) {
        try {
            String gid = aria2Service.addTorrentFromUrl(torrentUrl, "/downloads");
            return ResponseEntity.ok(gid);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{gid}")
    public ResponseEntity<String> removeDownload(
            @PathVariable String gid
    ) {
        try {
            aria2Service.removeDownload(gid);
            return ResponseEntity.ok("Download removed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
