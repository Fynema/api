package media.fynema.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import media.fynema.api.clients.Aria2Client;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class Aria2Service {
    private final Aria2Client client;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Set<String> activeGids = new HashSet<>();

    public String addTorrentFromUrl(String torrentUrl, String dir) throws Exception {
        byte[] torrentBytes = downloadBytes(torrentUrl);
        if (torrentBytes == null) throw new RuntimeException("Impossible de télécharger le fichier torrent");
        String b64 = Base64.getEncoder().encodeToString(torrentBytes);

        ArrayNode params = client.baseParams();
        params.add(b64);
        params.add(params.arrayNode());

        ObjectNode options = params.objectNode();
        if (dir != null && !dir.isBlank()) options.put("dir", "/downloads/" + dir);
        params.add(options);

        JsonNode resp = client.call("aria2.addTorrent", params);
        if (resp.has("error")) throw new RuntimeException(resp.get("error").toString());
        String gid = resp.get("result").asText();
        activeGids.add(gid);
        return gid;
    }

    public ObjectNode tellStatus(String gid) throws Exception {
        ArrayNode params = client.baseParams();
        params.add(gid);

        ArrayNode fields = params.arrayNode();
        fields.add("status");
        fields.add("totalLength");
        fields.add("completedLength");
        fields.add("files");
        params.add(fields);

        JsonNode resp = client.call("aria2.tellStatus", params);
        if (resp.has("error")) return null;
        return (ObjectNode) resp.get("result");
    }

    public String addTorrentFromUrl(String torrentUrl) throws Exception {
        return addTorrentFromUrl(torrentUrl, null);
    }

    public void removeDownload(String gid, String status) throws IOException, InterruptedException {
        if ("active".equals(status) || "error".equals(status)) {
            ArrayNode stopParams = client.baseParams();
            stopParams.add(gid);
            client.call("aria2.remove", stopParams);
            Thread.sleep(1500);
        }

        ArrayNode cleanParams = client.baseParams();
        cleanParams.add(gid);
        client.call("aria2.removeDownloadResult", cleanParams);
    }

    public void removeDownload(String gid) throws Exception {
        ObjectNode status = tellStatus(gid);
        String state = status != null && status.has("status") ? status.get("status").asText() : "complete";
        removeDownload(gid, state);
    }

    private Integer calculateProgress(ObjectNode status) {
        if (status == null || !status.has("files")) return null;
        long totalLength = status.get("totalLength").asLong();
        if (totalLength == 0) return null;

        long completedLength = 0;
        for (JsonNode fileNode : status.withArray("files")) {
            completedLength += fileNode.get("completedLength").asLong();
        }
        return (int) ((completedLength * 100) / totalLength);
    }

    @Scheduled(fixedDelay = 1000)
    public void cleanupCompletedDownloads() {
        Set<String> toRemove = new HashSet<>();
        for (String gid : activeGids) {
            try {
                ObjectNode status = tellStatus(gid);
                System.out.println("Status for GID " + gid + ": " + calculateProgress(status) + "%");

                if (status == null) {
                    toRemove.add(gid);
                    continue;
                }

                boolean allFilesCompleted = true;
                for (JsonNode file : status.withArray("files")) {
                    String fileCompleted = file.get("completedLength").asText();
                    String fileTotal = file.get("length").asText();
                    if (!fileCompleted.equals(fileTotal) || "0".equals(fileTotal)) {
                        allFilesCompleted = false;
                        break;
                    }
                }

                String state = status.get("status").asText();
                if ("error".equals(state) || allFilesCompleted) {
                    try {
                        removeDownload(gid, state);
                        System.out.println("Download supprimé pour GID: " + gid);
                    } catch (Exception ignored) {
                    }
                    toRemove.add(gid);
                }
            } catch (Exception ignored) {
                toRemove.add(gid);
            }
        }
        activeGids.removeAll(toRemove);
    }

    private byte[] downloadBytes(String url) {
        return restTemplate.getForObject(url, byte[].class);
    }
}
