package service.tasks;

import dto.SavedVoteDTO;
import service.api.ISenderService;

import javax.mail.MessagingException;

public class SendTask implements Runnable {

    private final SavedVoteDTO voteInfo;
    private final ISenderService senderService;
    private final int retries;
    private final long delay;
    private int currentAttempt = 0;


    public SendTask(SavedVoteDTO voteInfo, ISenderService senderService, int retries, long delay) {
        this.voteInfo = voteInfo;
        this.senderService = senderService;
        this.retries = retries;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (currentAttempt < retries) {
            try {
                senderService.send(voteInfo);
                return;
            } catch (Throwable e) {
                if (e instanceof MessagingException) {
                    handle(e);
                } else {
                    throw new RuntimeException("Unexpected error" +
                            " during message delivery", e);
                }
            }
        }
    }

    private void handle(Throwable e) {
        System.err.println("Attempt " + ++currentAttempt
                + " failed with message: " + e.getMessage());

        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("The message delivery thread" +
                    " was interrupted", ex);
        }
    }
}
