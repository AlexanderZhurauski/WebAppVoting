package web.controllers;

import dto.SavedVoteDTO;
import dto.VoteDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.api.IVoteService;

@RestController
public class VoteController {

    private final IVoteService service;

    public VoteController(IVoteService service) {
        this.service = service;
    }
    @PostMapping("/vote")
    public void postVote(@RequestBody VoteDTO vote) {
        this.service.validate(vote);
        SavedVoteDTO savedVote = new SavedVoteDTO(vote);
        this.service.save(savedVote);
    }
}
