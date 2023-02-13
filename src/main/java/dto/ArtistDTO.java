package dto;

import dao.entity.ArtistEntity;

import java.util.Objects;

public class ArtistDTO {

    private final long id;
    private final String name;

    public ArtistDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ArtistDTO(ArtistEntity entity) {
        this.id = entity.getId().intValue();
        this.name = entity.getArtist();
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
        ArtistDTO that = (ArtistDTO) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}