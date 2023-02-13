package dao.api;

import dao.entity.VoteEntity;
import dto.SavedVoteDTO;

import java.util.List;

public interface IVoteDAO {

    List<SavedVoteDTO> getAll();

    void save(SavedVoteDTO vote);
    void save (VoteEntity vote);
}