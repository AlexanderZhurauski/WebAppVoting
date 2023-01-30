package dto;

import java.time.LocalDateTime;

public class SavedVoteDTO {

    private final VoteDTO voteDTO;
    private final LocalDateTime creationTime;

    public SavedVoteDTO(VoteDTO voteDTO, LocalDateTime localDateTime) {
        this.voteDTO = voteDTO;
        this.creationTime = localDateTime;
    }

    public SavedVoteDTO(VoteDTO voteDTO) {
        this.voteDTO = voteDTO;
        creationTime = LocalDateTime.now();
    }

    public VoteDTO getVoteDTO() {
        return voteDTO;
    }

    public LocalDateTime getCreateDataTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "SavedVoteDTO{" +
                "voteDTO=" + voteDTO +
                ", creationTime=" + creationTime +
                '}';
    }
}