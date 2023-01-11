package dao;

import dao.api.IGenreDAO;
import dao.util.ConnectionManager;
import dto.GenreDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDBDAO implements IGenreDAO {
    private static final String SELECT_ALL = "SELECT id, name FROM app.genres;";
    private static final String SELECT_BY_ID = "SELECT id, name FROM app.genres" +
            " WHERE id = ?";
    private static final String ADD = "INSERT INTO app.genres (name) VALUES (?);";
    private static final String UPDATE = "UPDATE app.genres SET name=? WHERE id=?;";
    private static final String COUNT_VOTES = "SELECT COUNT(id) AS count FROM app.genres " +
            "GROUP BY id HAVING id=?;";
    private static final String DELETE = "DELETE FROM app.genres WHERE id=?;";
    @Override
    public List<GenreDTO> getAll() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement getAll = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = getAll.executeQuery()) {

            List<GenreDTO> genres = new ArrayList<>();
            while (resultSet.next()) {
                genres.add(new GenreDTO(getID(resultSet), getName(resultSet)));
            }
            return genres;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement exists = connection.prepareStatement(SELECT_BY_ID)) {
            exists.setInt(1, id);

            try (ResultSet resultSet = exists.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GenreDTO get(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement exists = connection.prepareStatement(SELECT_BY_ID)) {
            exists.setInt(1, id);

            try (ResultSet resultSet = exists.executeQuery()) {
                if (resultSet.next()) {
                    return new GenreDTO(getID(resultSet), getName(resultSet));
                } else {
                    throw new IllegalArgumentException(String
                            .format("No genre with id %d was found!", id));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(String genre) {
        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(ADD)) {
            statement.setString(1, genre);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int id, String genre) {
        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(UPDATE)) {
            statement.setString(1, genre);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection conn = ConnectionManager.open();
             PreparedStatement statement = conn.prepareStatement(COUNT_VOTES)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.getInt("count") == 0) {
                    try (PreparedStatement delStatement = conn.prepareStatement(DELETE)) {
                        delStatement.setInt(1, id);
                        delStatement.executeUpdate();
                    }
                } else {
                    throw new IllegalArgumentException("genre can't be deleted: it has votes");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("id");
    }

    private String getName(ResultSet resultSet) throws SQLException {
        return resultSet.getString("name");
    }
}
