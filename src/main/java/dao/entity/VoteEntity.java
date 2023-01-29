package dao.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "votes", schema = "app")
public class VoteEntity {
    @Id
    @GeneratedValue(generator = "votes_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "votes_seq",sequenceName = "votes_id_seq",schema = "app",allocationSize = 1)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "artist_id")
    private ArtistEntity artistId;
    @ManyToMany(mappedBy = "votes")
    private List<GenreEntity> genreIds = new ArrayList<>();
    private String about;
    private LocalDateTime creation_time;
    private String email;

    public VoteEntity(ArtistEntity artistEntity, String about,
                      LocalDateTime creation_time, String email) {
        this.artistId = artistEntity;
        this.about = about;
        this.creation_time = creation_time;
        this.email = email;
    }

    public VoteEntity(){
    }

    public Long getId() {
        return id;
    }

    public ArtistEntity getArtistId() {
        return artistId;
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

    public void setArtistId(ArtistEntity artistId) {
        this.artistId = artistId;
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

    public List<GenreEntity> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<GenreEntity> genreIds) {
        this.genreIds = genreIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteEntity that = (VoteEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(artistId, that.artistId)
                && Objects.equals(about, that.about)
                && Objects.equals(creation_time, that.creation_time)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artistId, about, creation_time, email);
    }
}
