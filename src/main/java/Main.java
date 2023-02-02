import dao.entity.ArtistEntity;
import dao.entity.GenreEntity;
import dao.entity.VoteEntity;
import dao.factories.ConnectionSingleton;
import dto.SavedVoteDTO;
import dto.VoteDTO;
import service.VoteService;
import service.factories.VoteServiceSingleton;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManager open = ConnectionSingleton.getInstance().open();

        open.getTransaction().begin();


        open.getTransaction().commit();
        open.close();
    }
}
