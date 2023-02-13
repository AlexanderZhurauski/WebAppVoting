package dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RankedArtistDTO {

    @JsonUnwrapped
    private ArtistDTO artist;
    private Long votes;

    public RankedArtistDTO() {
    }

    public RankedArtistDTO(ArtistDTO artist, Long votes) {
        this.artist = artist;
        this.votes = votes;
    }

    public ArtistDTO getArtist() {
        return artist;
    }

    public void setArtist(ArtistDTO artist) {
        this.artist = artist;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }
}
