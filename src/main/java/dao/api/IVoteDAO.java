package dao.api;

import dao.entity.VoteEntity;

import java.util.List;

public interface IVoteDAO {

    List<VoteEntity> getAll();
    void save (VoteEntity vote);
}