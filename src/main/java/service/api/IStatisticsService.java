package service.api;

import dto.ArtistDTO;
import dto.GenreDTO;
import dto.StatisticsDTO;

import java.time.LocalDateTime;
import java.util.Map;

public interface IStatisticsService {

    Map<ArtistDTO, Long> getBestArtists();

    Map<GenreDTO, Long> getBestGenres();

    Map<LocalDateTime, String> getAbouts();

    StatisticsDTO getStatistics();
}