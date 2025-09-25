package media.fynema.api.services;

import lombok.RequiredArgsConstructor;
import media.fynema.api.model.TorrentResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JackettService {

    @Value("${jackett.api.url}")
    private String jackettApiUrl;

    @Value("${jackett.api.key}")
    private String jackettApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<TorrentResult> searchMovie(String title, String year, List<String> filters) {
        String url = UriComponentsBuilder.fromUriString(jackettApiUrl + "/api/v2.0/indexers/all/results/torznab")
                .queryParam("apikey", jackettApiKey)
                .queryParam("t", "movie")
                .queryParam("q", title)
                .queryParam("year", year)
                .toUriString();

        String xmlResponse = restTemplate.getForObject(url, String.class);
        List<TorrentResult> results = parseTorznabResults(xmlResponse);

        if (filters != null && !filters.isEmpty()) {
            results = results.stream()
                    .filter(t -> filters.stream()
                            .allMatch(f -> t.getTitle().toLowerCase().contains(f.toLowerCase())))
                    .filter(t -> parseSeeders(t.getSeeders()) > 0)
//                    Exclude titles with year ranges like "1999-2000"
                    .filter(t -> !t.getTitle().matches(".*\\b(\\d{4})-(\\d{4})\\b.*"))
                    .toList();
        }

        return results.stream()
                .sorted(
                        Comparator.comparingInt((TorrentResult t) -> parseSeeders(t.getSeeders())).reversed()
                )
                .toList();
    }

    private List<TorrentResult> parseTorznabResults(String xml) {
        List<TorrentResult> results = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String title = getTagValue("title", item);
                String link = getTagValue("link", item);
                String size = getTagValue("size", item);
                if (size == null) {
                    size = getTorznabAttr(item, "size");
                }
                String tracker = getTagValue("jackettindexer", item);
                String seeders = getTorznabAttr(item, "seeders");

                results.add(new TorrentResult(title, link, size, tracker, seeders));
            }
        } catch (Exception e) {
            System.err.println("❌ Error while parsing Jackett XML: " + e.getMessage());
            throw new RuntimeException("Failed to parse Jackett response", e);
        }
        return results;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    private String getTorznabAttr(Element item, String attrName) {
        NodeList attributes = item.getElementsByTagName("torznab:attr");
        for (int j = 0; j < attributes.getLength(); j++) {
            Element attr = (Element) attributes.item(j);
            if (attrName.equals(attr.getAttribute("name"))) {
                return attr.getAttribute("value");
            }
        }
        return null;
    }

    private int parseSeeders(String seeders) {
        try {
            return Integer.parseInt(seeders);
        } catch (Exception e) {
            return 0;
        }
    }
}
