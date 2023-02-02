package service.api;

import dto.SavedVoteDTO;

public interface ISendingService {

    void initializeSendingService();

    void stopSendingService();

    void confirmVote(SavedVoteDTO vote);

    void verifyEmail(String email, String verificationLink);
}