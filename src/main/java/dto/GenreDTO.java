package dto;

import dao.entity.GenreEntity;

import java.util.Objects;

public class GenreDTO {

    private final long id;
    private final String genre;

    public GenreDTO(int id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public GenreDTO(GenreEntity entity) {
        this.id = entity.getId().intValue();
        this.genre = entity.getGenre();
    }

    public long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreDTO genreDTO = (GenreDTO) o;
        return id == genreDTO.id && genre.equals(genreDTO.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genre);
    }
}