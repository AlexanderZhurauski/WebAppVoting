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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VoteDBDAO implements IVoteDAO {
    @Override
    public List<SavedVoteDTO> getAll() {
        EntityManager open = ConnectionSingleton.getInstance().open();
        open.getTransaction().begin();
        List<SavedVoteDTO> savedVoteDTOs;
        List<VoteEntity> voteEntities;

        Query query = open.createQuery("SELECT v FROM VoteEntity v");
        voteEntities = query.getResultList();

        savedVoteDTOs = voteEntities
                .stream()
                .map(vote -> new SavedVoteDTO(new VoteDTO(
                    vote.getArtistId().getId(),
                    vote.getGenreIds().stream().map(GenreEntity::getId).collect(Collectors.toList()),
                    vote.getAbout(),
                    vote.getEmail()
                    ), vote.getCreation_time()))
                .collect(Collectors.toList());

        open.getTransaction().commit();
        open.close();

        return savedVoteDTOs;
    }
    @Override
    public void save(SavedVoteDTO vote) {
        EntityManager open = ConnectionSingleton.getInstance().open();
        open.getTransaction().begin();

        ArtistEntity artistEntity = open.find(ArtistEntity.class,vote.getVoteDTO().getArtistId());
        System.out.println(artistEntity.getId());

        List<Long> genreIds = vote.getVoteDTO().getGenreIds();
        Query getGenres = open.createQuery("SELECT g FROM GenreEntity g WHERE id IN (:genreIds)");
        getGenres.setParameter("genreIds", genreIds);
        List<GenreEntity> genres = getGenres.getResultList();

        VoteEntity voteEntity = new VoteEntity(artistEntity, vote.getVoteDTO().getAbout(),
                vote.getCreateDataTime(), vote.getVoteDTO().getEmail());
        artistEntity.getVotes().add(voteEntity);
        genres.forEach(genre -> genre.addVote(voteEntity));

        open.getTransaction().commit();
        open.close();
    }
}
