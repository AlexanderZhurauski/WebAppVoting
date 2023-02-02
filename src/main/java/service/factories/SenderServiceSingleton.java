package service.factories;

import dao.factories.DAOType;
import dao.factories.EmailSendingDAOSingleton;
import service.EmailSendingService;
import service.api.ISendingService;

public class SenderServiceSingleton {

    private volatile static ISendingService instance = null;

    private SenderServiceSingleton() {
    }

    public static ISendingService getInstance() {
        if (instance == null) {
            synchronized (SenderServiceSingleton.class) {
                if (instance == null) {
                    instance = new EmailSendingService(
                            GenreServiceSingleton.getInstance(),
                            ArtistServiceSingleton.getInstance(),
                            EmailSendingDAOSingleton.getInstance(DAOType.DB));
                }
            }
        }
        return instance;
    }
}