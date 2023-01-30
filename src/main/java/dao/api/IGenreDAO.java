package dao.api;

import dto.GenreDTO;

import java.util.List;

public interface IGenreDAO {

    List<GenreDTO> getAll();

    boolean exists(long id);

    GenreDTO get(long id);

    void add(String genre);

    void update(long id, String genre);
    
    void delete(long id);
}