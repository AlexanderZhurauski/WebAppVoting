package dao.entity;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table (name = "artists", schema = "app")
public class ArtistEntity {
    @Id
    @GeneratedValue(generator = "artists_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name ="artists_seq",sequenceName = "artists_id_seq",schema = "app",allocationSize = 1)
    private Long id;
    @Version
    @Column(name = "version")
    private Long version;
    @Column(name = "name")
    private String artist;

    public ArtistEntity(){
    }
    public ArtistEntity(String artist){
        this.artist=artist;
    }
    public ArtistEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getArtist() {
        return this.artist;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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
        ArtistEntity that = (ArtistEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(version, that.version) && Objects.equals(artist, that.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, artist);
    }
}
