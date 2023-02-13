package dto;

import dao.entity.GenreEntity;

import java.util.Objects;

public class GenreDTO {

    private final Long id;
    private final Long version;
    private final String name;

    public GenreDTO(Long id, Long version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public GenreDTO(GenreEntity entity) {
        this.id = entity.getId();
        this.version = entity.getVersion();
        this.name = entity.getGenre();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    public Long getVersion() {
        return this.version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreDTO genreDTO = (GenreDTO) o;
        return this.id == genreDTO.id && this.name.equals(genreDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }
}