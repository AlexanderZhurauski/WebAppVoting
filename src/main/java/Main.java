import dao.api.IVoteDAO;
import dao.database.VoteDBDAO;
import dao.factories.VoteDAOSingleton;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        VoteDBDAO voteDAO = new VoteDBDAO();

        VoteDTO voteDTO = new VoteDTO(3L, List.of(2L,4L,9L), "Halo", "gedddf@gmil.com");
        SavedVoteDTO savedVote = new SavedVoteDTO(voteDTO, LocalDateTime.now());
        voteDAO.saveCriteria(savedVote);

        System.out.println("----------------");

        List<SavedVoteDTO> savedVotes = voteDAO.getAllCriteria();
        savedVotes.forEach(System.out::println);
     }
}
