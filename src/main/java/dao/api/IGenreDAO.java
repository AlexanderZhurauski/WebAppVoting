package dao.api;

import dto.GenreDTO;

import java.util.List;

public interface IGenreDAO {

    List<GenreDTO> getAll();

    boolean exists(Long id);

    GenreDTO get(Long id);

    void add(String genre);

    void update(Long id, String genre);
    
    void delete(Long id);
}