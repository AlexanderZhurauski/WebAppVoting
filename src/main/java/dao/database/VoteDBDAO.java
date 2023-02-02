package dao.database;

import dao.api.IVoteDAO;
import dao.entity.ArtistEntity;
import dao.entity.GenreEntity;
import dao.entity.VoteEntity;
import dao.factories.ConnectionSingleton;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class VoteDBDAO implements IVoteDAO {
    @Override
    public List<SavedVoteDTO> getAll() {
        List<SavedVoteDTO> list = new ArrayList<>();
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        list = entityManager.createQuery("FROM VoteEntity ", VoteEntity.class)
                .getResultList()
                .stream()
                .map(SavedVoteDTO::new)
                .toList();

        entityManager.getTransaction().commit();
        return list;
    }

    @Override
    public void save(SavedVoteDTO vote) {
        VoteDTO voteDTO=vote.getVoteDTO();
        VoteEntity voteEntity=new VoteEntity(vote);
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, voteDTO.getArtistId());
        voteEntity.setArtistEntity(artistEntity);
        voteDTO.getGenreIds()
                .forEach(l->voteEntity.addGenre(entityManager.find(GenreEntity.class,l)));
        entityManager.persist(voteEntity);
        entityManager.getTransaction().commit();
    }
}
