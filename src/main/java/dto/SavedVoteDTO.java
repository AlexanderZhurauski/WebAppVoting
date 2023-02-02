package dto;

import dao.entity.VoteEntity;

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

    public SavedVoteDTO(VoteEntity entity){
        this.voteDTO=new VoteDTO(entity);
        this.creationTime=entity.getCreation_time();
    }
    public VoteDTO getVoteDTO() {
        return voteDTO;
    }

    public LocalDateTime getCreateDataTime() {
        return creationTime;
    }
}