package dao.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "votes", schema = "app")
public class VoteEntity {
    @Id
    @GeneratedValue(generator = "votes_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "votes_seq",sequenceName = "votes_id_seq",schema = "app",allocationSize = 1)
    private Long id;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id")
    private ArtistEntity artist_id;
    private String about;
    private LocalDateTime creation_time;
    private String email;

    public VoteEntity(ArtistEntity artistEntity, String about,
                      LocalDateTime creation_time, String email) {
        this.artist_id = artistEntity;
        this.about = about;
        this.creation_time = creation_time;
        this.email = email;
    }

    public VoteEntity(){
    }

    public Long getId() {
        return id;
    }

    public ArtistEntity getArtist_id() {
        return artist_id;
    }

    public String getAbout() {
        return about;
    }

    public LocalDateTime getCreation_time() {
        return creation_time;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setArtist_id(ArtistEntity artist_id) {
        this.artist_id = artist_id;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setCreation_time(LocalDateTime creation_time) {
        this.creation_time = creation_time;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteEntity that = (VoteEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(artist_id, that.artist_id)
                && Objects.equals(about, that.about)
                && Objects.equals(creation_time, that.creation_time)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artist_id, about, creation_time, email);
    }
}
