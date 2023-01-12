package dao;

import dao.api.IVoteDAO;
import dao.util.ConnectionManager;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VoteDBDAO implements IVoteDAO {

    private static final String SELECT_ALL = "SELECT id, artist_id, about, creation_time " +
            "FROM app.votes;";
    private static final String SELECT_GENRES = "SELECT vg.genre_id AS id " +
            "FROM app.votes AS v " +
            "  INNER JOIN app.votes_genres AS vg " +
            "  ON v.id = vg.vote_id " +
            "WHERE v.id = ?;";
    private static final String SAVE_VOTE = "INSERT INTO app.votes (" +
            "artist_id, about, creation_time) " +
            "VALUES (?, ?, ?);";
    private static final String SAVE_GENRE_VOTE = "INSERT INTO app.votes_genres (" +
            "vote_id, genre_id) " +
            "VALUES (?, ?);";
    @Override
    public List<SavedVoteDTO> getAll() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement getAll = connection.prepareStatement(SELECT_ALL);
             PreparedStatement getGenres = connection.prepareStatement(SELECT_GENRES);
             ResultSet voteResults = getAll.executeQuery()) {

            List<SavedVoteDTO> votes = new ArrayList<>();
            int id;
            int artistID;
            LocalDateTime time;
            String about;
            VoteDTO vote;

            while (voteResults.next()) {
                id = getID(voteResults);
                getGenres.setInt(1, id);
                List<Integer> genreIDs = new ArrayList<>();
                try (ResultSet genreResults = getGenres.executeQuery()) {
                    while (genreResults.next()) {
                        genreIDs.add(getGenreID(genreResults));
                    }
                }
                artistID = getArtistID(voteResults);
                time = getTime(voteResults);
                about = getAbout(voteResults);
                vote = new VoteDTO(artistID, genreIDs, about);
                votes.add(new SavedVoteDTO(vote, time));
            }
            return votes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(SavedVoteDTO vote) {
        try (Connection connection = ConnectionManager.open()) {

            connection.setAutoCommit(false);

            try (PreparedStatement saveVote = connection.prepareStatement(SAVE_VOTE,
                    Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement saveGenreVote = connection.prepareStatement(SAVE_GENRE_VOTE)) {

                VoteDTO innerVote = vote.getVoteDTO();
                saveVote.setInt(1, innerVote.getArtistId());
                saveVote.setString(2, innerVote.getAbout());
                saveVote.setObject(3, vote.getCreateDataTime());
                saveVote.execute();

                List<Integer> genres = innerVote.getGenreIds();
                int voteID;
                try (ResultSet generatedID = saveVote.getGeneratedKeys()) {
                    if (generatedID.next()) {
                        voteID = generatedID.getInt(1);
                    } else {
                        throw new IllegalArgumentException("Failed to save the vote");
                    }
                }

                for (int i = 0; i < genres.size(); i++) {
                    saveGenreVote.setInt(1, voteID);
                    saveGenreVote.setInt(2, genres.get(i));
                    saveGenreVote.execute();
                }

                connection.commit();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(1);
    }

    private int getGenreID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(1);
    }
    private int getArtistID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(2);
    }

    private String getAbout(ResultSet resultSet) throws SQLException {
        return resultSet.getString(3);
    }

    private LocalDateTime getTime(ResultSet resultSet) throws SQLException {
        return resultSet.getObject(4, LocalDateTime.class);
    }
}
