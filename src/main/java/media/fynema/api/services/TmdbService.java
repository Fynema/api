package media.fynema.api.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TmdbService {
    private final RestClient restClient = RestClient.create();

    @Value("${tmdb.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    public TMDBResponse searchMovie(String query) {
        return restClient.get()
                .uri(BASE_URL + "/search/movie?language=fr-FR&query={query}", query)
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .body(TMDBResponse.class);
    }

    @Data
    public static class TMDBResponse {
        private int page;
        private List<MovieResult> results;
        @JsonProperty("total_results")
        private int totalResults;
        @JsonProperty("total_pages")
        private int totalPages;
    }

    @Data
    public static class MovieResult {
        private int id;
        private String title;
        private String name;
        @JsonProperty("original_title")
        private String originalTitle;
        @JsonProperty("original_name")
        private String originalName;
        @JsonProperty("release_date")
        private String releaseDate;
        @JsonProperty("first_air_date")
        private String firstAirDate;
        private String overview;
        @JsonProperty("poster_path")
        private String posterPath;
    }
}
