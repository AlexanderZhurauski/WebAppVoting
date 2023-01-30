package dto;

import java.util.Collections;
import java.util.List;

public class VoteDTO {

    private final String email;
    private final long artistId;
    private final List<Long> genreIds;
    private final String about;

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