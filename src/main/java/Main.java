import dao.api.IVoteDAO;
import dao.entity.ArtistEntity;
import dao.entity.GenreEntity;
import dao.entity.VoteEntity;
import dao.factories.ConnectionSingleton;
import dao.factories.VoteDAOSingleton;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IVoteDAO voteDAO = VoteDAOSingleton.getInstance();
        /*
        VoteDTO voteDTO = new VoteDTO(3L, List.of(2L,4L,9L), "Halo", "gef@gmil.com");
        SavedVoteDTO savedVote = new SavedVoteDTO(voteDTO, LocalDateTime.now());
        voteDAO.save(savedVote);
         */
        List<SavedVoteDTO> savedVotes = voteDAO.getAll();
        savedVotes.forEach(System.out::println);
     }
}
