package dao.api;

import dao.entity.GenreEntity;

import java.util.List;

public interface IGenreDAO {

    List<GenreEntity> getAll();

    boolean exists(Long id);

    GenreEntity get(Long id);

    void add(GenreEntity genre);

    void update(GenreEntity genre);
    
    void delete(Long id);
}