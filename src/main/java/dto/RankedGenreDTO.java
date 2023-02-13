package dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RankedGenreDTO {

    @JsonUnwrapped
    private GenreDTO genre;
    private Long votes;

    public RankedGenreDTO() {
    }

    public RankedGenreDTO(GenreDTO genre, Long votes) {
        this.genre = genre;
        this.votes = votes;
    }

    public GenreDTO getGenre() {
        return genre;
    }

    public void setGenre(GenreDTO genre) {
        this.genre = genre;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }
}
