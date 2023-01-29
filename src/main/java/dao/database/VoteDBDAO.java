package dao.database;

import dao.api.IVoteDAO;
import dao.entity.ArtistEntity;
import dao.entity.GenreEntity;
import dao.entity.VoteEntity;
import dao.factories.ConnectionSingleton;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

public class VoteDBDAO implements IVoteDAO {
    @Override
    public List<SavedVoteDTO> getAll() {
        List<SavedVoteDTO> savedVoteDTOs;
        List<VoteEntity> voteEntities;
        EntityManager entityManager = null;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            Query query = entityManager.createQuery("SELECT v FROM VoteEntity v");
            voteEntities = query.getResultList();
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
        savedVoteDTOs = voteEntities
                .stream()
                .map(vote -> new SavedVoteDTO(new VoteDTO(
                        vote.getArtistId().getId(),
                        vote.getGenreIds().stream().map(GenreEntity::getId).collect(Collectors.toList()),
                        vote.getAbout(),
                        vote.getEmail()
                ), vote.getCreationTime()))
                .collect(Collectors.toList());

        return savedVoteDTOs;
    }
    @Override
    public void save(SavedVoteDTO vote) {
        EntityManager entityManager = null;
        try {
            entityManager = ConnectionSingleton.getInstance().open();

            entityManager.getTransaction().begin();

            ArtistEntity artistEntity = entityManager.find(ArtistEntity.class,vote.getVoteDTO().getArtistId());
            System.out.println(artistEntity.getId());

            List<Long> genreIds = vote.getVoteDTO().getGenreIds();
            Query getGenres = entityManager.createQuery("SELECT g FROM GenreEntity g WHERE id IN (:genreIds)");
            getGenres.setParameter("genreIds", genreIds);
            List<GenreEntity> genres = getGenres.getResultList();

            VoteEntity voteEntity = new VoteEntity(artistEntity, vote.getVoteDTO().getAbout(),
                    vote.getCreateDataTime(), vote.getVoteDTO().getEmail());
            artistEntity.getVotes().add(voteEntity);
            genres.forEach(genre -> genre.addVote(voteEntity));

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
