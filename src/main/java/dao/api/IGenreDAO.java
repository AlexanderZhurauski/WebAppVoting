package dao.api;

import dto.GenreDTO;
import dto.GenreInputDTO;

import java.util.List;

public interface IGenreDAO {

    List<GenreDTO> getAll();

    boolean exists(Long id);

    GenreDTO get(Long id);

    void add(GenreInputDTO genre);

    void update(Long id, GenreInputDTO genre);
    
    void delete(Long id);
}