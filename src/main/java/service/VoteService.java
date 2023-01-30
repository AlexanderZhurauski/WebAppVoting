package service;

import dao.api.IVoteDAO;
import dto.SavedVoteDTO;
import dto.VoteDTO;
import service.api.IArtistService;
import service.api.IGenreService;
import service.api.ISenderService;
import service.api.IVoteService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VoteService implements IVoteService {

    private final IVoteDAO voteDAO;
    private final IGenreService genreService;
    private final IArtistService artistService;
    private final ISenderService senderService;
    private final ExecutorService executorService;
    private static final String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
            + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public VoteService(IVoteDAO voteDAO,
                       IGenreService genreService,
                       IArtistService artistService,
                       ISenderService senderService
    ) {
        this.voteDAO = voteDAO;
        this.genreService = genreService;
        this.artistService = artistService;
        this.senderService = senderService;
        this.executorService = Executors.newFixedThreadPool(8);
    }

    @Override
    public List<SavedVoteDTO> getAll() {
        return voteDAO.getAll();
    }

    @Override
    public void save(SavedVoteDTO vote) {
        voteDAO.save(vote);
    }

    @Override
    public void validate(VoteDTO vote) {
        long artistId = vote.getArtistId();
        validateArtist(artistId);

        List<Long> genresIdList = vote.getGenreIds();
        validateGenres(genresIdList);

        String about = vote.getAbout();
        validateAbout(about);

        String email = vote.getEmail();
        validateEmail(email);
    }

    private void validateArtist(long artistId) {
        if (!artistService.exists(artistId)) {
            throw new NoSuchElementException("Invalid artist id " +
                    "provided - '" + artistId + "' ");
        }
    }

    private void validateGenres(List<Long> genresIdList) {

        if (genresIdList.size() < 3 || genresIdList.size() > 5) {
            throw new IllegalArgumentException("Number of genres outside" +
                    " allowed range (3-5) ");
        }
        if (genresIdList.size() > genresIdList.stream()
                                              .distinct()
                                              .count()) {
            throw new IllegalArgumentException("Genre parameter " +
                    "must be non-repeating ");
        }
        for (long genreId : genresIdList) {
            if (!genreService.exists(genreId)) {
                throw new NoSuchElementException("Invalid genre id " +
                        "provided - '" + genreId + "' ");
            }
        }
    }

    private void validateAbout(String about) {
        if (about == null || about.isBlank()) {
            throw new IllegalArgumentException("User failed to provide" +
                    " a message ");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("User failed to provide" +
                    " an email");
        }
        if (!email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("User provided " +
                    "an invalid email");
        }
    }
}