package service.api;

import dto.GenreDTO;

import java.util.List;

public interface IGenreService {

    List<GenreDTO> getAll();

    GenreDTO get(long id);

    boolean exists(long id);

    void add(String genre);

    void update(long id, String genre);

    void delete(long id);
}