package dao.database;

import dao.api.IVoteDAO;
import dao.factories.ConnectionSingleton;
import dto.SavedVoteDTO;
import dto.VoteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VoteDBDAO implements IVoteDAO {

    private static final String SELECT_ALL = "SELECT id, artist_id, about, email, creation_time " +
            "FROM app.votes;";
    private static final String SELECT_GENRES = "SELECT vg.genre_id " +
            "FROM app.votes AS v " +
            "  INNER JOIN app.votes_genres AS vg " +
            "  ON v.id = vg.vote_id " +
            "WHERE v.id = ?;";
    private static final String SAVE_VOTE = "INSERT INTO app.votes (" +
            "artist_id, about, email, creation_time) " +
            "VALUES (?, ?, ?, ?);";
    private static final String SAVE_GENRE_VOTE = "INSERT INTO app.votes_genres (" +
            "vote_id, genre_id) " +
            "VALUES (?, ?);";

    @Override
    public List<SavedVoteDTO> getAll() {
        try (Connection connection = ConnectionSingleton.getInstance().open();
             PreparedStatement getAll = connection.prepareStatement(SELECT_ALL);
             PreparedStatement getGenres = connection.prepareStatement(SELECT_GENRES);
             ResultSet voteResults = getAll.executeQuery()) {

            List<SavedVoteDTO> votes = new ArrayList<>();
            int id;
            int artistID;
            LocalDateTime time;
            String about;
            String email;
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
                email = getEmail(voteResults);
                vote = new VoteDTO(artistID, genreIDs, about, email);
                votes.add(new SavedVoteDTO(vote, time));
            }
            return votes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(SavedVoteDTO vote) {
        try (Connection connection = ConnectionSingleton.getInstance().open()) {

            connection.setAutoCommit(false);

            try (PreparedStatement saveVote = connection.prepareStatement(SAVE_VOTE,
                    Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement saveGenreVote = connection.prepareStatement(SAVE_GENRE_VOTE)) {

                VoteDTO innerVote = vote.getVoteDTO();
                saveVote.setInt(1, innerVote.getArtistId());
                saveVote.setString(2, innerVote.getAbout());
                saveVote.setString(3, innerVote.getEmail());
                saveVote.setObject(4, vote.getCreateDataTime());
                saveVote.execute();

                List<Integer> genres = innerVote.getGenreIds();
                int voteID;
                try (ResultSet generatedID = saveVote.getGeneratedKeys()) {
                    if (generatedID.next()) {
                        voteID = generatedID.getInt(1);
                    } else {
                        connection.rollback();
                        throw new IllegalArgumentException("Failed to save the vote");
                    }
                }

                try {
                    for (Integer genre : genres) {
                        saveGenreVote.setInt(1, voteID);
                        saveGenreVote.setInt(2, genre);
                        saveGenreVote.execute();
                    }
                } catch (SQLException e) {
                    connection.rollback();
                    throw new IllegalArgumentException("Failed to save the vote");
                }

                connection.commit();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("id");
    }

    private int getGenreID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("genre_id");
    }

    private int getArtistID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("artist_id");
    }

    private String getAbout(ResultSet resultSet) throws SQLException {
        return resultSet.getString("about");
    }

    private String getEmail(ResultSet resultSet) throws SQLException {
        return resultSet.getString("email");
    }

    private LocalDateTime getTime(ResultSet resultSet) throws SQLException {
        return resultSet.getObject("creation_time", LocalDateTime.class);
    }
}
