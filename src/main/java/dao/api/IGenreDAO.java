package dao.api;

import dto.GenreDTO;

import java.util.List;

public interface IGenreDAO {

    List<GenreDTO> getAll();

    boolean exists(int id);

    GenreDTO get(int id);

    void add(String artist);

    void update(int id, String artist);

    void delete(int id);
}