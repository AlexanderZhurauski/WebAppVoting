package service;

import dao.api.IGenreDAO;
import dao.entity.GenreEntity;
import dto.GenreDTO;
import dto.GenreInputDTO;
import service.api.IGenreService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GenreService implements IGenreService {

    private final IGenreDAO dataSource;

    public GenreService(IGenreDAO dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<GenreDTO> getAll() {
        return convertFromEntityList(dataSource.getAll());
    }

    @Override
    public GenreDTO get(Long id) {
        return convertFromEntity(dataSource.get(id));
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
            dataSource.add(convertToEntity(genre));
        }
    }

    @Override
    public void update(Long id, Long version, GenreInputDTO genre) {
        GenreEntity genreEntity = dataSource.get(id);
        if (genreEntity == null) {
            throw new IllegalArgumentException("No genre updated for id " +
                    id + " as it doesn't exist.");
        }
        if (!Objects.equals(genreEntity.getVersion(), version)) {
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
        genreEntity.setGenre(genre.getName());

        dataSource.update(genreEntity);
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

    private GenreDTO convertFromEntity(GenreEntity genre) {
        return new GenreDTO(genre);
    }

    private List<GenreDTO> convertFromEntityList(List<GenreEntity> genres) {
        return genres
                .stream()
                .map(this::convertFromEntity)
                .collect(Collectors.toList());
    }

    private GenreEntity convertToEntity(GenreInputDTO genre) {
        return new GenreEntity(genre.getName());
    }
}