package dao;

import dao.api.IGenreDAO;
import dao.factories.ConnectionFactory;
import dto.GenreDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenrePostgresDAO implements IGenreDAO {

    private ConnectionFactory factory = ConnectionFactory.getInstance();
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS app.genres( " +
            "id BIGSERIAL, " +
            "name VARCHAR(30), " +
            "CONSTRAINT pk_id PRIMARY KEY (id));";
    private static final String INITIALIZE_TABLE = "INSERT INTO app.genres (name) " +
            "VALUES ('Rock'), ('Pop'), ('Rap'), ('Techno'), ('Jazz'), " +
            "('Classic Rock'), ('Country'), ('Hard Rock'), ('Blues'), ('Hip Hop');";
    private static final String SELECT_ALL = "SELECT id, name FROM app.genres;";
    private static final String SELECT_BY_ID = "SELECT id, name FROM app.genres" +
            " WHERE id = ?";

    public GenrePostgresDAO() {
        try (Connection connection = factory.getConnection();
             PreparedStatement create = connection.prepareStatement(CREATE_TABLE);
             PreparedStatement initialize = connection.prepareStatement(INITIALIZE_TABLE)) {

            create.execute();
            initialize.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GenreDTO> getAll() {
        try (Connection connection = factory.getConnection();
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
        try (Connection connection = factory.getConnection();
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
        try (Connection connection = factory.getConnection();
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

    private int getID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("id");
    }

    private String getName(ResultSet resultSet) throws SQLException {
        return resultSet.getString("name");
    }
}
