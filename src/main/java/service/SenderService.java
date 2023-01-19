package service;

import dao.util.PropertiesUtil;
import dto.SavedVoteDTO;
import dto.VoteDTO;
import service.api.IArtistService;
import service.api.IGenreService;
import service.api.ISenderService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class SenderService implements ISenderService {

    private final IGenreService genreService;
    private final IArtistService artistService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
    private static final String TOPIC = "WebAppVoting Vote Confirmation";
    private final String SENDER;
    private final String PASSWORD;
    private final Properties mailProperties = new Properties();

    public SenderService(IGenreService genreService,
                         IArtistService artistService) {
        this.genreService = genreService;
        this.artistService = artistService;

        this.SENDER = PropertiesUtil.get("sender");
        this.PASSWORD = PropertiesUtil.get("password");

        mailProperties.put("mail.transport.protocol",
                PropertiesUtil.get("mail.transport.protocol"));
        mailProperties.put("mail.host", PropertiesUtil.get("mail.host"));
        mailProperties.put("mail.smtp.auth",
                PropertiesUtil.get("mail.smtp.auth"));
        mailProperties.put("mail.smtp.port",
                PropertiesUtil.get("mail.smtp.port"));
        mailProperties.put("mail.debug",
                PropertiesUtil.get("mail.debug"));
        mailProperties.put("mail.smtp.socketFactory.port",
                PropertiesUtil.get("mail.smtp.socketFactory.port"));
        mailProperties.put("mail.smtp.socketFactory.class",
                PropertiesUtil.get("mail.smtp.socketFactory.class"));
        mailProperties.put("mail.smtp.socketFactory.fallback",
                PropertiesUtil.get("mail.smtp.socketFactory.fallback"));
        mailProperties.put("mail.smtp.starttls.enable",
                PropertiesUtil.get("mail.smtp.starttls.enable"));
    }

    @Override
    public void send(SavedVoteDTO vote) {
        StringBuilder messageText = new StringBuilder();
        String recipient = vote.getVoteDTO()
                .getEmail();
        MimeMessage message = createMessage(vote, messageText);;

        try {
            message.setFrom(new InternetAddress(SENDER));
            message.setRecipients(Message.RecipientType.TO,
                    recipient);
            message.setSubject(TOPIC);
            message.setText(messageText.toString());
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send to send " +
                    "the confirmation email", e);
        }
    }

    private MimeMessage createMessage(SavedVoteDTO vote, StringBuilder messageText) {
        Authenticator authenticator = new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER,
                        PASSWORD);
            }
        };
        Session session = Session.getInstance(mailProperties,
                authenticator);
        MimeMessage message = new MimeMessage(session);

        VoteDTO voteDTO = vote.getVoteDTO();
        messageText.append("Thank you for submitting your vote!\n");
        appendGenreNames(voteDTO, messageText);
        appendArtistName(voteDTO, messageText);
        appendAbout(voteDTO, messageText);
        messageText.append("Vote date: ");
        messageText.append(vote.getCreateDataTime().format(formatter));

        return message;
    }

    private void appendGenreNames(VoteDTO voteDTO, StringBuilder message) {
        message.append("Your genre vote:\n");
        List<Integer> genreIDs = voteDTO.getGenreIds();
        for (int i = 0; i < genreIDs.size(); i++) {
            message.append(i + 1)
                    .append(". ")
                    .append(this.genreService.get(genreIDs.get(i)).getGenre())
                    .append("\n");
        }
    }

    private void appendArtistName(VoteDTO voteDTO, StringBuilder message) {
        message.append("Your artist vote:\n");
        int artistID = voteDTO.getArtistId();
        message.append("1. ")
                .append(this.artistService.get(artistID).getArtist())
                .append("\n");
    }

    private void appendAbout(VoteDTO voteDTO, StringBuilder message) {
        message.append("Your about text:\n");
        message.append(voteDTO.getAbout())
                .append("\n");
    }
}
