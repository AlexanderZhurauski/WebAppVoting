package dao.database;

import dao.api.IVoteDAO;
import dao.entity.ArtistEntity;
import dao.entity.GenreEntity;
import dao.entity.VoteEntity;
import dao.util.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VoteDBDAO implements IVoteDAO {

    private final ConnectionManager connectionManager;

    public VoteDBDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    @Override
    public List<VoteEntity> getAll() {
        List<VoteEntity> voteEntities;
        EntityManager entityManager = null;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<VoteEntity> voteQuery = cb.createQuery(VoteEntity.class);

            Root<VoteEntity> from = voteQuery.from(VoteEntity.class);
            from.fetch("genreIds", JoinType.INNER);
            CriteriaQuery<VoteEntity> finalVoteQuery = voteQuery.select(from);

            voteEntities = entityManager.createQuery(finalVoteQuery)
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return voteEntities;
    }
    @Override
    public void save(VoteEntity vote) {
        EntityManager entityManager = connectionManager.open();
        try {
            entityManager.getTransaction().begin();

            ArtistEntity artist = entityManager.find(ArtistEntity.class, vote.getArtistId().getId());
            vote.setArtistId(artist);

            List<GenreEntity> genres = vote.getGenreIds();
            vote.setGenreIds(new ArrayList<>());
            genres.stream()
                    .map(GenreEntity::getId)
                    .forEach(id -> vote.getGenreIds().add(entityManager.find(GenreEntity.class, id)));

            entityManager.persist(vote);

            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}
