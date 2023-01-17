package service;

import dto.SavedVoteDTO;
import dto.VoteDTO;
import service.api.IArtistService;
import service.api.IGenreService;
import service.api.ISenderService;
import service.factories.MessageFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

public class SenderService implements ISenderService {

    private IGenreService genreService;
    private IArtistService artistService;
    private MessageFactory messageFactory;

    public SenderService(IGenreService genreService,
                         IArtistService artistService) {
        this.genreService = genreService;
        this.artistService = artistService;
        this.messageFactory = MessageFactory.getInstance();
    }

    @Override
    public void send(SavedVoteDTO vote) {
        StringBuilder messageText = new StringBuilder();
        formatMessage(vote, messageText);
        String recipient = vote.getVoteDTO()
                .getEmail();
        MimeMessage message = this.messageFactory.getMessage();

        try {
            message.setFrom(new InternetAddress(this.messageFactory.getSender()));
            message.setRecipients(Message.RecipientType.TO,
                    recipient);
            message.setText(messageText.toString());
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send to send " +
                    "the confirmation email", e);
        }
    }

    private void formatMessage(SavedVoteDTO vote, StringBuilder message) {
        VoteDTO voteDTO = vote.getVoteDTO();
        message.append("Thank you for submitting your vote!\n");
        appendGenreNames(voteDTO, message);
        appendArtistName(voteDTO, message);
        appendAbout(voteDTO, message);
        message.append("Vote date: ");
        message.append(vote.getCreateDataTime());
    }

    private void appendGenreNames(VoteDTO voteDTO, StringBuilder message) {
        message.append("Your genre vote:\n");
        List<Integer> genreIDs = voteDTO.getGenreIds();
        for (int i = 0; i < genreIDs.size(); i++) {
            message.append(++i)
                    .append(". ")
                    .append(this.genreService.get(genreIDs.get(i)))
                    .append("\n");
        }
    }

    private void appendArtistName(VoteDTO voteDTO, StringBuilder message) {
        message.append("Your artist vote:\n");
        int artistID = voteDTO.getArtistId();
        message.append("1. ")
                .append(this.artistService.get(artistID))
                .append("\n");
    }

    private void appendAbout(VoteDTO voteDTO, StringBuilder message) {
        message.append("Your about text:\n");
        int artistID = voteDTO.getArtistId();
        message.append(this.artistService.get(artistID))
                .append("\n");
    }
}
