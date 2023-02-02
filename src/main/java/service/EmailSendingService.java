package service;

import dao.api.IEmailSendingDAO;
import dao.util.PropertiesUtil;
import dao.entity.EmailEntity;
import dao.entity.EmailStatus;
import dto.SavedVoteDTO;
import dto.VoteDTO;
import service.api.IArtistService;
import service.api.IGenreService;
import service.api.ISendingService;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EmailSendingService implements ISendingService {

    private final IGenreService genreService;
    private final IArtistService artistService;
    private final IEmailSendingDAO emailSendingDAO;
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
    private final ScheduledExecutorService executorService;
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_VOTE_CONFIRMATION_SENDS = 3;
    private static final int MAX_SENDS_VERIFICATION_LINK = 1;
    private static final long INTERVAL_BETWEEN_SHIPMENTS = 10L;

    public EmailSendingService(IGenreService genreService,
                               IArtistService artistService,
                               IEmailSendingDAO emailSendingDAO) {
        this.genreService = genreService;
        this.artistService = artistService;
        this.emailSendingDAO = emailSendingDAO;
        this.executorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

        this.SENDER = PropertiesUtil.get(SENDER_PROMPT);
        this.PASSWORD = PropertiesUtil.get(PASSWORD_PROMPT);

        prepareMailProperties();
    }

    @Override
    public void confirmVote(SavedVoteDTO vote) {
        String messageText = createVoteConfirmationText(vote);
        String recipient = vote.getVoteDTO().getEmail();
        EmailEntity email = new EmailEntity(recipient, CONFIRMATION_SUBJECT, messageText,
                MAX_VOTE_CONFIRMATION_SENDS, EmailStatus.WAITING);
        emailSendingDAO.add(email);
    }

    @Override
    public void verifyEmail(String email, String messageText) {
        EmailEntity emailEntity = new EmailEntity(email, VALIDATION_SUBJECT, messageText,
                MAX_SENDS_VERIFICATION_LINK, EmailStatus.WAITING);
        emailSendingDAO.add(emailEntity);
    }

    @Override
    public void initializeSendingService() {
        executorService.scheduleWithFixedDelay(() -> {
            List<EmailEntity> emails = emailSendingDAO.getUnsent();
            for (EmailEntity email : emails) {
                email.setStatus(EmailStatus.SENT);
                email.setDepartures(email.getDepartures() - 1);
                emailSendingDAO.update(email);
                executorService.submit(() -> {
                    try {
                        send(email);
                        email.setStatus(EmailStatus.SUCCESS);
                    } catch (AddressException e) {
                        email.setStatus(EmailStatus.ERROR);
                        throw new RuntimeException("Failed to send the confirmation " +
                                "email due to wrongly formatted address ", e);
                    } catch (MessagingException e) {
                        email.setStatus(EmailStatus.WAITING);
                        throw new RuntimeException("Failed to send " +
                                "the confirmation email", e);
                    } finally {
                        emailSendingDAO.update(email);
                    }
                });
            }
        }, INTERVAL_BETWEEN_SHIPMENTS, INTERVAL_BETWEEN_SHIPMENTS, TimeUnit.SECONDS);
    }

    @Override
    public void stopSendingService() {
        this.executorService.shutdown();
    }

    private void send(EmailEntity email)
            throws MessagingException {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER, PASSWORD);
            }
        };
        Session session = Session.getInstance(mailProperties,
                authenticator);
        MimeMessage message = new MimeMessage(session);
        InternetAddress address = new InternetAddress(SENDER);
        message.setFrom(address);
        message.setRecipients(Message.RecipientType.TO,
                email.getRecipient());
        message.setSubject(email.getTopic());
        message.setText(email.getTextMessage());
        Transport.send(message);
    }

    private void prepareMailProperties() {
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
        List<Integer> genreIDs = voteDTO.getGenreIds();
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
        int artistID = voteDTO.getArtistId();
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