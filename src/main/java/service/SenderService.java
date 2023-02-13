package service;

import dao.api.IEmailDAO;
import dao.entity.GenreEntity;
import dao.entity.VoteEntity;
import dao.util.PropertiesUtil;
import dao.entity.EmailEntity;
import dao.entity.EmailStatus;
import dto.EmailDTO;
import dto.SavedVoteDTO;
import dto.VoteDTO;
import service.api.IArtistService;
import service.api.IGenreService;
import service.api.ISenderService;
import service.tasks.EmailSendingTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SenderService implements ISenderService {

    private final IGenreService genreService;
    private final IArtistService artistService;
    private final IEmailDAO emailDAO;
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
    private final EmailSendingTask sendingThread;
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_VOTE_CONFIRMATION_SENDS = 3;
    private static final int MAX_SENDS_VERIFICATION_LINK = 1;
    private static final long INTERVAL_BETWEEN_SHIPMENTS = 10L;

    public SenderService(IGenreService genreService,
                               IArtistService artistService,
                               IEmailDAO emailDAO) {
        this.genreService = genreService;
        this.artistService = artistService;
        this.emailDAO = emailDAO;
        this.executorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        this.sendingThread = new EmailSendingTask(executorService, emailDAO, this);

        this.SENDER = PropertiesUtil.get(SENDER_PROMPT);
        this.PASSWORD = PropertiesUtil.get(PASSWORD_PROMPT);

        prepareMailProperties();
    }

    @Override
    public void initializeSendingService() {
        executorService.scheduleWithFixedDelay(sendingThread,
                INTERVAL_BETWEEN_SHIPMENTS,
                INTERVAL_BETWEEN_SHIPMENTS,
                TimeUnit.SECONDS);
    }

    @Override
    public void stopSendingService() {
        this.executorService.shutdown();
    }

    @Override
    public void confirmVote(VoteEntity vote) {
        SavedVoteDTO voteDTO = convertToVoteDTO(vote);
        String messageText = createVoteConfirmationText(voteDTO);
        String recipient = voteDTO.getVoteDTO().getEmail();
        EmailEntity email = new EmailEntity(vote, recipient, CONFIRMATION_SUBJECT,
                messageText, MAX_VOTE_CONFIRMATION_SENDS, EmailStatus.WAITING);
        emailDAO.add(email);
    }

    @Override
    public void verifyEmail(EmailDTO email) {
        EmailEntity emailEntity = new EmailEntity(new VoteEntity(), email.getEmail(), VALIDATION_SUBJECT,
                email.getMessage(), MAX_SENDS_VERIFICATION_LINK, EmailStatus.WAITING);
        emailDAO.add(emailEntity);
    }

    @Override
    public void send(EmailEntity email) throws MessagingException {
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
        List<Long> genreIDs = voteDTO.getGenreIds();
        for (int i = 0; i < genreIDs.size(); i++) {
            message.append(i + 1)
                    .append(". ")
                    .append(this.genreService.get(genreIDs.get(i)).getName())
                    .append("\n");
        }
        return message.toString();
    }

    private String getFormattedArtistName(VoteDTO voteDTO) {
        StringBuilder message = new StringBuilder();
        message.append("Your artist vote:\n");
        long artistID = voteDTO.getArtistId();
        message.append("1. ")
                .append(this.artistService.get(artistID).getName())
                .append("\n");
        return message.toString();
    }

    private String getFormattedAbout(VoteDTO voteDTO) {
        return "Your about text:\n" +
                voteDTO.getAbout() +
                "\n";
    }

    private SavedVoteDTO convertToVoteDTO(VoteEntity vote) {
        return new SavedVoteDTO(
                new VoteDTO(
                        vote.getArtistId().getId(),
                        vote.getGenreIds()
                                .stream()
                                .map(GenreEntity::getId)
                                .collect(Collectors.toList()),
                        vote.getAbout(),
                        vote.getEmail()
                ), vote.getCreationTime()
        );
    }
}
