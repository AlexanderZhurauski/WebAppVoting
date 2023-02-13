package dto;

import java.util.Collections;
import java.util.List;

public class VoteDTO {

    private String email;
    private long artistId;
    private List<Long> genreIds;
    private String about;

    public VoteDTO() {
    }

    public VoteDTO(long artistId, List<Long> genreIds, String about, String email) {
        this.artistId = artistId;
        this.genreIds = genreIds;
        this.about = about;
        this.email = email;
    }

    public long getArtistId() {
        return artistId;
    }

    public List<Long> getGenreIds() {
        return Collections.unmodifiableList(genreIds);
    }

    public String getAbout() {
        return about;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
                "email='" + email + '\'' +
                ", artistId=" + artistId +
                ", genreIds=" + genreIds +
                ", about='" + about + '\'' +
                '}';
    }

}