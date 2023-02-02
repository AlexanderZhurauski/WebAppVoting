package dao.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "genres", schema = "app")
public class GenreEntity {
    @Id
    @GeneratedValue(generator = "genre_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "genre_seq", sequenceName = "genre_id_seq", schema = "app", allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String genre;
   // @ManyToMany(mappedBy = "genreEntities")
   // private List<VoteEntity> voteEntities= new ArrayList<>();

    public GenreEntity() {
    }

    public GenreEntity(String genre) {
        this.genre = genre;
    }

    //public void addVote(VoteEntity vote){
    //    voteEntities.add(vote);
    //}

    public Long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    //public List<VoteEntity> getVoteEntities() {
    //    return voteEntities;
    //}

    //public void setVoteEntities(List<VoteEntity> voteEntities) {
    //    this.voteEntities = voteEntities;
    //}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreEntity that = (GenreEntity) o;
        return id == that.id && Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genre);
    }
}
