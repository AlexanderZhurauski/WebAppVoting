package dao.database;

import dao.api.IVoteDAO;
import dao.entity.ArtistEntity;
import dao.entity.GenreEntity;
import dao.entity.GenreEntity_;
import dao.entity.VoteEntity;
import dao.util.ConnectionManager;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

public class VoteDBDAO implements IVoteDAO {

    private ConnectionManager connectionManager;

    public VoteDBDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    @Override
    public List<SavedVoteDTO> getAll() {
        List<SavedVoteDTO> savedVoteDTOs;
        List<VoteEntity> voteEntities;
        EntityManager entityManager = null;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<VoteEntity> voteQuery = cb.createQuery(VoteEntity.class);

            Root<VoteEntity> from = voteQuery.from(VoteEntity.class);
            CriteriaQuery<VoteEntity> finalVoteQuery = voteQuery.select(from);

            voteEntities = entityManager.createQuery(finalVoteQuery)
                    .getResultList();
            savedVoteDTOs = voteEntities
                    .stream()
                    .map(vote -> new SavedVoteDTO(new VoteDTO(
                            vote.getArtistId().getId(),
                            vote.getGenreIds().stream().map(GenreEntity::getId).collect(Collectors.toList()),
                            vote.getAbout(),
                            vote.getEmail()
                    ), vote.getCreationTime()))
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

        return savedVoteDTOs;
    }
    @Override
    public void save(SavedVoteDTO vote) {

        EntityManager entityManager = null;
        List<GenreEntity> genres;
        try {
            entityManager = connectionManager.open();

            entityManager.getTransaction().begin();

            ArtistEntity artistEntity = entityManager.find(ArtistEntity.class,
                    vote.getVoteDTO().getArtistId());

            List<Long> genreIds = vote.getVoteDTO().getGenreIds();
            System.out.println(genreIds);

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<GenreEntity> genreQuery = cb.createQuery(GenreEntity.class);

            Root<GenreEntity> fromGenres = genreQuery.from(GenreEntity.class);
            CriteriaQuery<GenreEntity> finalGenreQuery = genreQuery.select(fromGenres)
                    .where(fromGenres.get(GenreEntity_.id).in(genreIds));

            genres = entityManager.createQuery(finalGenreQuery).getResultList();

            VoteEntity voteEntity = new VoteEntity(artistEntity, vote.getVoteDTO().getAbout(),
                    genres, vote.getCreateDataTime(), vote.getVoteDTO().getEmail());
            entityManager.persist(voteEntity);

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
    }
}
