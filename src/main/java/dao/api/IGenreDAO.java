package dao.api;

import dto.GenreDTO;

import java.util.List;

public interface IGenreDAO {

    List<GenreDTO> getAll();

    boolean exists(int id);

    GenreDTO get(int id);

    void add(String genre);

    void update(int id, String genre);

    void delete(int id);
}