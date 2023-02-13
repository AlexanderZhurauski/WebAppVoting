package service.api;

import dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IStatisticsService {

    List<RankedArtistDTO> getBestArtists();

    List<RankedGenreDTO> getBestGenres();

    Map<LocalDateTime, String> getAbouts();

    StatisticsDTO getStatistics();
}