import dao.api.IArtistDAO;
import dao.api.IGenreDAO;
import dao.api.IVoteDAO;
import dao.database.VoteDBDAO;
import dao.entity.ArtistEntity;
import dao.factories.ArtistDAOSingleton;
import dao.factories.GenreDAOSingleton;
import dao.factories.VoteDAOSingleton;
import dto.ArtistDTO;
import dto.GenreDTO;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
        VoteDBDAO voteDAO = new VoteDBDAO();

        VoteDTO voteDTO = new VoteDTO(3L, List.of(2L,4L,9L), "Halo", "gedddf@gmil.com");
        SavedVoteDTO savedVote = new SavedVoteDTO(voteDTO, LocalDateTime.now());
        voteDAO.saveCriteria(savedVote);

        System.out.println("----------------");

        List<SavedVoteDTO> savedVotes = voteDAO.getAllCriteria();
        savedVotes.forEach(System.out::println);

         */
        IArtistDAO artistDAO = ArtistDAOSingleton.getInstance();
        IGenreDAO genreDAO = GenreDAOSingleton.getInstance();

        System.out.println(genreDAO.exists(1));

        GenreDTO genreDTO = genreDAO.get(1);
        System.out.println(genreDTO.getGenre());

        List<GenreDTO> genres = genreDAO.getAll();
        genres.stream().map(GenreDTO::getGenre).forEach(System.out::println);
     }
}
