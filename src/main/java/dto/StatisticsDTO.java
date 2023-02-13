package dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatisticsDTO {

    private final List<RankedArtistDTO> bestArtists;
    private final List<RankedGenreDTO> bestGenres;
    private final Map<LocalDateTime, String> abouts;

    public StatisticsDTO(List<RankedArtistDTO> bestArtists,
                         List<RankedGenreDTO> bestGenres,
                         Map<LocalDateTime, String> abouts) {
        this.bestArtists = bestArtists;
        this.bestGenres = bestGenres;
        this.abouts = abouts;
    }

    public List<RankedArtistDTO> getBestArtists() {
        return bestArtists;
    }

    public List<RankedGenreDTO> getBestGenres() {
        return bestGenres;
    }

    public Map<LocalDateTime, String> getAbouts() {
        return abouts;
    }
}