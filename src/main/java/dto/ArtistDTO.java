package dto;

import dao.entity.ArtistEntity;

import java.util.Objects;

public class ArtistDTO {

    private final Long id;
    private final Long version;
    private final String name;

    public ArtistDTO(Long id, Long version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public ArtistDTO(ArtistEntity entity) {
        this.id = entity.getId();
        this.version = entity.getVersion();
        this.name = entity.getArtist();
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
        ArtistDTO that = (ArtistDTO) o;
        return this.id == that.id && this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }
}