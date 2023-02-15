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
    @GeneratedValue(generator = "votes_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "votes_seq", sequenceName = "votes_id_seq", schema = "app", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private ArtistEntity artistId;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "votes_genres", schema = "app",
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<GenreEntity> genreIds = new ArrayList<>();
    private String about;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    private String email;

    public VoteEntity(ArtistEntity artistEntity, String about,
                      List<GenreEntity> genreIds,
                      LocalDateTime creationTime, String email) {
        this.artistId = artistEntity;
        this.genreIds = genreIds;
        this.about = about;
        this.creationTime = creationTime;
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

    public LocalDateTime getCreationTime() {
        return creationTime;
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

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
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
                && Objects.equals(creationTime, that.creationTime)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artistId, about, creationTime, email);
    }
}
