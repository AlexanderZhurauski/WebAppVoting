package service.api;

import dto.GenreDTO;
import dto.GenreInputDTO;

import java.util.List;

public interface IGenreService {

    List<GenreDTO> getAll();

    GenreDTO get(Long id);

    boolean exists(Long id);

    void add(GenreInputDTO genre);

    void update(Long id, Long version, GenreInputDTO genre);

    void delete(Long id);
}