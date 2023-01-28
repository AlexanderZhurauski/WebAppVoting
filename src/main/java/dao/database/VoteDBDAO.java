package dao.database;

import dao.api.IVoteDAO;
import dto.SavedVoteDTO;

import java.util.List;

public class VoteDBDAO implements IVoteDAO {
    @Override
    public List<SavedVoteDTO> getAll() {
        return null;
    }
    @Override
    public void save(SavedVoteDTO vote) {

    }
}
