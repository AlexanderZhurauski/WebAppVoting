package service.tasks;

import dao.api.IEmailDAO;
import dao.entity.EmailEntity;
import dao.entity.EmailStatus;
import service.api.ISenderService;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EmailSendingTask implements Runnable {

    private final ScheduledExecutorService executorService;
    private final IEmailDAO emailDAO;
    private final ISenderService senderService;
    private static final long PAUSE_SENDING_EMAIL = 10L;
    private static final long MILLISECONDS_TO_SEND_EMAIL = 100L;

    public EmailSendingTask(ScheduledExecutorService executorService,
                              IEmailDAO emailDAO,
                              ISenderService senderService) {
        this.executorService = executorService;
        this.emailDAO = emailDAO;
        this.senderService = senderService;
    }

    @Override
    public void run() {
        List<EmailEntity> emails = emailDAO.getUnsent();
        int countExceptions = 0;
        for (EmailEntity email : emails) {
            email.setStatus(EmailStatus.SENT);
            email.setDepartures(email.getDepartures() - 1);
            emailDAO.update(email);
            try {
                executorService.submit(() -> {
                    try {
                        senderService.send(email);
                        email.setStatus(EmailStatus.SUCCESS);
                    } catch (SendFailedException e) {
                        Address[] invalidAddresses = e.getInvalidAddresses();
                        email.setStatus(invalidAddresses == null
                                ? EmailStatus.WAITING
                                : EmailStatus.ERROR);
                        throw new RuntimeException("Failed to send the confirmation " +
                                "email due to wrongly formatted address ", e);
                    } catch (MessagingException e) {
                        email.setStatus(EmailStatus.WAITING);
                        throw new RuntimeException("Failed to send " +
                                "the confirmation email", e);
                    } finally {
                        emailDAO.update(email);
                    }
                }).get(MILLISECONDS_TO_SEND_EMAIL, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                countExceptions++;
            }
        }
        if (countExceptions == emails.size() && emails.size() != 0) {
            try {
                TimeUnit.MINUTES.sleep(PAUSE_SENDING_EMAIL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
