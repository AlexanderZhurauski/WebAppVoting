package service.api;

import dao.entity.EmailEntity;
import dao.entity.VoteEntity;
import dto.EmailDTO;
import dto.SavedVoteDTO;

import javax.mail.MessagingException;

public interface ISenderService {
    void initializeSendingService();

    void stopSendingService();

    void confirmVote(VoteEntity vote);

    void verifyEmail(EmailDTO email);

    void send(EmailEntity email) throws MessagingException;
}
