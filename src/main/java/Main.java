import dao.api.IArtistDAO;
import dao.api.IGenreDAO;
import dao.api.IVoteDAO;
import dao.database.ArtistDBDao;
import dao.database.GenreDBDAO;
import dao.database.VoteDBDAO;
import dao.entity.ArtistEntity;
import dao.factories.ArtistDAOSingleton;
import dao.factories.ConnectionSingleton;
import dao.factories.GenreDAOSingleton;
import dao.factories.VoteDAOSingleton;
import dao.util.ConnectionManager;
import dto.ArtistDTO;
import dto.GenreDTO;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        VoteDBDAO voteDAO = new VoteDBDAO();

        VoteDTO voteDTO = new VoteDTO(3L, List.of(2L,4L,9L), "Halo", "gedf@gmil.com");
        SavedVoteDTO savedVote = new SavedVoteDTO(voteDTO, LocalDateTime.now());
        voteDAO.saveCriteria(savedVote);

        System.out.println("----------------");

        List<SavedVoteDTO> savedVotes = voteDAO.getAllCriteria();
        savedVotes.forEach(System.out::println);
     }
}
