package dao.entity;

import dto.SavedVoteDTO;
import dto.VoteDTO;

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
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistEntity artist_id;
    private String about;
    private LocalDateTime creation_time;
    private String email;
    @ManyToMany
    @JoinTable(name = "votes_genres",
            schema = "app",
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<GenreEntity> genreEntities = new ArrayList<>();

    public VoteEntity(ArtistEntity artistEntity, String about,
                      LocalDateTime creation_time, String email,
                      List<GenreEntity> genreEntities) {
        this.artist_id = artistEntity;
        this.about = about;
        this.creation_time = creation_time;
        this.email = email;
        this.genreEntities = genreEntities;
    }

    public VoteEntity() {
    }

    public VoteEntity(SavedVoteDTO savedVoteDTO){
        this.creation_time=savedVoteDTO.getCreateDataTime();
        VoteDTO voteDTO=savedVoteDTO.getVoteDTO();
        this.about=voteDTO.getAbout();
        this.email=voteDTO.getEmail();
    }
    public Long getId() {
        return id;
    }

    public void addGenre(GenreEntity genre) {
        genreEntities.add(genre);
    }

    public ArtistEntity getArtistEntity() {
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

    public List<GenreEntity> getGenreEntities() {
        return genreEntities;
    }

    public void setArtistEntity(ArtistEntity artistEntity) {
        this.artist_id = artistEntity;
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

    public void setGenreEntities(List<GenreEntity> genreEntities) {
        this.genreEntities = genreEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteEntity that = (VoteEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(about, that.about)
                && Objects.equals(creation_time, that.creation_time)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, about, creation_time, email);
    }
}
