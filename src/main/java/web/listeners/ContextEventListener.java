package web.listeners;


import dao.api.IConnection;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import service.api.ISenderService;

@Component
public class ContextEventListener {

    private ISenderService senderService;
    private IConnection connectionManager;

    public ContextEventListener(ISenderService senderService) {
        this.senderService = senderService;
    }

    @EventListener
    public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
        this.senderService.initializeSendingService();
    }

    @EventListener
    public void contextDestroyed(ContextClosedEvent event) throws Exception {
        this.senderService.stopSendingService();
        this.connectionManager.close();
    }
}
