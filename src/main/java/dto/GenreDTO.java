package dto;

import dao.entity.GenreEntity;

import java.util.Objects;

public class GenreDTO {

    private final long id;
    private final String name;

    public GenreDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public GenreDTO(GenreEntity entity) {
        this.id = entity.getId().intValue();
        this.name = entity.getGenre();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreDTO genreDTO = (GenreDTO) o;
        return id == genreDTO.id && name.equals(genreDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}