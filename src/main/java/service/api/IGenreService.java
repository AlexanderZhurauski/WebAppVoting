package service.api;

import dto.GenreDTO;

import java.util.List;

public interface IGenreService {

    List<GenreDTO> getAll();

    GenreDTO get(Long id);

    boolean exists(Long id);

    void add(String genre);

    void update(Long id, String genre);

    void delete(Long id);
}