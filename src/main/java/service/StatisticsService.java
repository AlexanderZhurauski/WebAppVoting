package service;

import dto.ArtistDTO;
import dto.GenreDTO;
import dto.SavedVoteDTO;
import dto.StatisticsDTO;
import dto.VoteDTO;
import service.api.IGenreService;
import service.api.IArtistService;
import service.api.IStatisticsService;
import service.api.IVoteService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsService implements IStatisticsService {

    private final IVoteService voteService;
    private final IGenreService genreService;
    private final IArtistService artistService;

    public StatisticsService(IVoteService voteService,
                             IGenreService genreService,
                             IArtistService artistService) {
        this.voteService = voteService;
        this.genreService = genreService;
        this.artistService = artistService;
    }

    @Override
    public Map<ArtistDTO, Long> getBestArtists() {
        final Map<Long, Long> artistVotes = artistService.getAll()
                .stream()
                .collect(Collectors.toMap(ArtistDTO::getId, artist -> 0L));
        voteService.getAll()
                .stream()
                .map(SavedVoteDTO::getVoteDTO)
                .map(VoteDTO::getArtistId)
                .forEach(artistId -> artistVotes.put(
                        artistId,
                        artistVotes.get(artistId) + 1L));
        return sortArtistsByVotes(artistVotes);
    }

    private Map<ArtistDTO, Long> sortArtistsByVotes(Map<Long, Long> artists) {
        return artists.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        entry -> artistService.get(entry.getKey()),
                        Map.Entry::getValue,
                        Long::sum,
                        LinkedHashMap::new));
    }

    @Override
    public Map<GenreDTO, Long> getBestGenres() {
        final Map<Long, Long> genreVotes = genreService.getAll()
                .stream()
                .collect(Collectors.toMap(GenreDTO::getId, genre -> 0L));
        voteService.getAll()
                .stream()
                .map(SavedVoteDTO::getVoteDTO)
                .map(VoteDTO::getGenreIds)
                .flatMap(Collection::stream)
                .forEach(genreId -> genreVotes.put(
                        genreId,
                        genreVotes.get(genreId) + 1L));
        return sortGenresByVotes(genreVotes);
    }

    private Map<GenreDTO, Long> sortGenresByVotes(Map<Long, Long> genres) {
        return genres.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        entry -> genreService.get(entry.getKey()),
                        Map.Entry::getValue,
                        Long::sum,
                        LinkedHashMap::new));
    }

    @Override
    public Map<LocalDateTime, String> getAbouts() {
        List<SavedVoteDTO> votes = voteService.getAll();
        return votes.stream()
                .sorted(Comparator.comparing(SavedVoteDTO::getCreateDataTime))
                .collect(Collectors.toMap(
                        SavedVoteDTO::getCreateDataTime,
                        vote -> vote.getVoteDTO().getAbout(),
                        (value1, value2) -> value1 + "\n\n"+ value2,
                        LinkedHashMap::new));
    }

    @Override
    public StatisticsDTO getStatistics() {
        return new StatisticsDTO(getBestArtists(), getBestGenres(), getAbouts());
    }
}