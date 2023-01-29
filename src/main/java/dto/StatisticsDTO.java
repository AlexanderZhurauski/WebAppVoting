package dto;

import java.time.LocalDateTime;
import java.util.Map;

public class StatisticsDTO {

    private final Map<ArtistDTO, Long> bestArtists;
    private final Map<GenreDTO, Long> bestGenres;
    private final Map<LocalDateTime, String> abouts;

    public StatisticsDTO(Map<ArtistDTO, Long> bestArtists,
                         Map<GenreDTO, Long> bestGenres,
                         Map<LocalDateTime, String> abouts) {
        this.bestArtists = bestArtists;
        this.bestGenres = bestGenres;
        this.abouts = abouts;
    }

    public Map<ArtistDTO, Long> getBestArtists() {
        return bestArtists;
    }

    public Map<GenreDTO, Long> getBestGenres() {
        return bestGenres;
    }

    public Map<LocalDateTime, String> getAbouts() {
        return abouts;
    }
}