package service;

import dao.util.PropertiesUtil;
import dto.SavedVoteDTO;
import dto.VoteDTO;
import service.api.IArtistService;
import service.api.IGenreService;
import service.api.ISenderService;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class SenderService implements ISenderService {

    private final IGenreService genreService;
    private final IArtistService artistService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
    private static final String SENDER_PROMPT = "sender";
    private static final String PASSWORD_PROMPT = "password";
    private static final String CONFIRMATION_SUBJECT = "WebAppVoting Vote Confirmation";
    private static final String VALIDATION_SUBJECT = "WebAppVoting Email Validation";
    private static final String TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private static final String SERVICE_HOST = "mail.host";
    private static final String SMPT_AUTHENTICATION = "mail.smtp.auth";
    private static final String SMPT_PORT = "mail.smtp.port";
    private static final String DEBUG_ON = "mail.debug";
    private static final String SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";
    private static final String SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";
    private static final String SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
    private static final String ENABLE_START_TLS = "mail.smtp.starttls.enable";


    private final String SENDER;
    private final String PASSWORD;
    private final Properties mailProperties = new Properties();

    public SenderService(IGenreService genreService,
                         IArtistService artistService) {
        this.genreService = genreService;
        this.artistService = artistService;

        this.SENDER = PropertiesUtil.get(SENDER_PROMPT);
        this.PASSWORD = PropertiesUtil.get(PASSWORD_PROMPT);

        mailProperties.put(TRANSPORT_PROTOCOL,
                PropertiesUtil.get(TRANSPORT_PROTOCOL));
        mailProperties.put(SERVICE_HOST, PropertiesUtil.get(SERVICE_HOST));
        mailProperties.put(SMPT_AUTHENTICATION,
                PropertiesUtil.get(SMPT_AUTHENTICATION));
        mailProperties.put(SMPT_PORT, PropertiesUtil.get(SMPT_PORT));
        mailProperties.put(DEBUG_ON, PropertiesUtil.get(DEBUG_ON));
        mailProperties.put(SOCKET_FACTORY_PORT,
                PropertiesUtil.get(SOCKET_FACTORY_PORT));
        mailProperties.put(SOCKET_FACTORY_CLASS,
                PropertiesUtil.get(SOCKET_FACTORY_CLASS));
        mailProperties.put(SOCKET_FACTORY_FALLBACK,
                PropertiesUtil.get(SOCKET_FACTORY_FALLBACK));
        mailProperties.put(ENABLE_START_TLS,
                PropertiesUtil.get(ENABLE_START_TLS));
    }

    @Override
    public void sendVoteConfirmation(SavedVoteDTO vote) {
        String recipient = vote.getVoteDTO().getEmail();
        String messageText = createVoteConfirmationText(vote);
        try {
            send(recipient, CONFIRMATION_SUBJECT, messageText);
        } catch (AddressException e) {
            throw new RuntimeException("Failed to send the confirmation email " +
                    "due to wrongly formatted address ", e);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send " +
                    "the confirmation email", e);
        }

    }

    @Override
    public void sendVerificationLink(String email, String verificationLink) {
        try {
            send(email, VALIDATION_SUBJECT, verificationLink);
        } catch (AddressException e) {
            throw new RuntimeException("Failed to send the validation email " +
                    "due to wrongly formatted address ", e);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send " +
                    "the validation email", e);
        }

    }

    private void send(String recipient, String subject, String messageText) throws MessagingException {
        Authenticator authenticator = new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER,
                        PASSWORD);
            }
        };
        Session session = Session.getInstance(mailProperties,
                authenticator);
        MimeMessage message = new MimeMessage(session);
        InternetAddress address = new InternetAddress(SENDER);
        message.setFrom(address);
        message.setRecipients(Message.RecipientType.TO,
                recipient);
        message.setSubject(subject);
        message.setText(messageText);
        Transport.send(message);
    }

    private String createVoteConfirmationText(SavedVoteDTO vote) {
        StringBuilder messageText = new StringBuilder();
        VoteDTO voteDTO = vote.getVoteDTO();
        messageText.append("Thank you for submitting your vote!\n");
        messageText.append(getFormattedGenreNames(voteDTO));
        messageText.append(getFormattedArtistName(voteDTO));
        messageText.append(getFormattedAbout(voteDTO));
        messageText.append("Vote date: ");
        messageText.append(vote.getCreateDataTime().format(formatter));

        return messageText.toString();
    }


    private String getFormattedGenreNames(VoteDTO voteDTO) {
        StringBuilder message = new StringBuilder();
        message.append("Your genre vote:\n");
        List<Long> genreIDs = voteDTO.getGenreIds();
        for (int i = 0; i < genreIDs.size(); i++) {
            message.append(i + 1)
                    .append(". ")
                    .append(this.genreService.get(genreIDs.get(i)).getGenre())
                    .append("\n");
        }
        return message.toString();
    }

    private String getFormattedArtistName(VoteDTO voteDTO) {
        StringBuilder message = new StringBuilder();
        message.append("Your artist vote:\n");
        Long artistID = voteDTO.getArtistId();
        message.append("1. ")
                .append(this.artistService.get(artistID).getArtist())
                .append("\n");
        return message.toString();
    }

    private String getFormattedAbout(VoteDTO voteDTO) {
        return "Your about text:\n" +
                voteDTO.getAbout() +
                "\n";
    }
}
