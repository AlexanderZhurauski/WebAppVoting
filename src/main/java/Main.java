import dao.entity.ArtistEntity;
import dao.entity.VoteEntity;
import dao.factories.ConnectionSingleton;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        EntityManager open = ConnectionSingleton.getInstance().open();
        open.getTransaction().begin();

        ArtistEntity artistEntity = open.find(ArtistEntity.class,1L);

        VoteEntity vote = new VoteEntity(artistEntity,
                "some str", LocalDateTime.now(), "mail@mail.ru");
        open.persist(vote);

        open.getTransaction().commit();
        open.close();
    }
}
