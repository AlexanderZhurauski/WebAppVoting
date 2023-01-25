package service.api;

import dto.SavedVoteDTO;

public interface ISenderService {
    void sendVoteConfirmation(SavedVoteDTO vote);

    void sendVerificationLink(String email, String verificationLink);
}
