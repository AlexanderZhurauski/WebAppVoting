package web.listeners;


import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import service.api.ISenderService;

@Component
public class EmailResendLoaderListener {

    private ISenderService senderService;

    public EmailResendLoaderListener(ISenderService senderService) {
        this.senderService = senderService;
    }

    @EventListener
    public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
        this.senderService.initializeSendingService();
    }

    @EventListener
    public void contextDestroyed(ContextClosedEvent event) {
        this.senderService.stopSendingService();
    }
}
