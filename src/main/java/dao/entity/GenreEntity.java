package dao.entity;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "genres", schema = "app")
public class GenreEntity {
    @Id
    @GeneratedValue(generator = "genres_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name ="genres_seq", sequenceName = "genres_id_seq", schema = "app", allocationSize = 1)
    private Long id;
    @Version
    @Column(name = "version")
    private Long version;
    @Column(name = "name")
    private String genre;
    public GenreEntity() {
    }

    public GenreEntity(String genre) {
        this.genre = genre;
    }

    public GenreEntity(Long id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public GenreEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getGenre() {
        return this.genre;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreEntity that = (GenreEntity) o;
        return id.equals(that.id) && version.equals(that.version) && genre.equals(that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, genre);
    }
}
