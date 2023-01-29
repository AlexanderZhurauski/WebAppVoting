package service;

import dao.api.IGenreDAO;
import dto.GenreDTO;
import service.api.IGenreService;

import java.util.List;

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
    public GenreDTO get(long id) {
        return dataSource.get(id);
    }

    @Override
    public boolean exists(long id) {
        return dataSource.exists(id);
    }

    @Override
    public void add(String genre) {
        boolean isDuplicate = isDuplicate(genre);
        if (isDuplicate) {
            throw new IllegalArgumentException("This genre has already " +
                    "been added");
        } else {
            dataSource.add(genre);
        }
    }

    @Override
    public void update(long id, String genre) {
        boolean isDuplicate = isDuplicate(genre);
        if (dataSource.exists(id) && !isDuplicate) {
            dataSource.update(id, genre);
        } else {
            throw new IllegalArgumentException("No genre updated for id " +
                    id + " or genre has already been added");
        }
    }

    @Override
    public void delete(long id) {
        if (dataSource.exists(id)) {
            dataSource.delete(id);
        } else {
            throw new IllegalArgumentException("No genre deleted for id " + id);
        }
    }

    private boolean isDuplicate(String genre){
        return getAll().stream()
                .map(GenreDTO::getGenre)
                .anyMatch(nameGenre -> nameGenre.equalsIgnoreCase(genre));
    }
}