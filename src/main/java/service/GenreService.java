package service;

import dao.api.IGenreDAO;
import dto.GenreDTO;
import dto.GenreInputDTO;
import service.api.IGenreService;

import java.util.List;
import java.util.Objects;

public class GenreService implements IGenreService {

    private final IGenreDAO dataSource;

    public GenreService(IGenreDAO dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<GenreDTO> getAll() {
        return dataSource.getAll();
    }

    @Override
    public GenreDTO get(Long id) {
        return dataSource.get(id);
    }

    @Override
    public boolean exists(Long id) {
        return dataSource.exists(id);
    }

    @Override
    public void add(GenreInputDTO genre) {
        boolean isDuplicate = isDuplicate(genre.getName());
        if (isDuplicate) {
            throw new IllegalArgumentException("This genre has already " +
                    "been added");
        } else {
            dataSource.add(genre);
        }
    }

    @Override
    public void update(Long id, Long version, GenreInputDTO genre) {
        GenreDTO genreDTO = dataSource.get(id);
        if (genreDTO == null) {
            throw new IllegalArgumentException("No genre updated for id " +
                    id + " as it doesn't exist.");
        }
        if (!Objects.equals(genreDTO.getVersion(), version)) {
            throw new IllegalArgumentException("No genre updated for id " +
                    id + ". Invalid version provided.");
        }
        String genreName = genre.getName();
        if (genreName == null || genreName.isBlank()) {
            throw new IllegalArgumentException("No genre name has been provided!");
        }
        if (isDuplicate(genreName)) {
            throw new IllegalArgumentException("Genre " + genreName
                    + " already exists!");
        }
        dataSource.update(id, genre);
    }

    @Override
    public void delete(Long id) {
        if (dataSource.exists(id)) {
            dataSource.delete(id);
        } else {
            throw new IllegalArgumentException("No genre deleted for id " + id);
        }
    }

    private boolean isDuplicate(String genre){
        return getAll().stream()
                .map(GenreDTO::getName)
                .anyMatch(nameGenre -> nameGenre.equalsIgnoreCase(genre));
    }
}