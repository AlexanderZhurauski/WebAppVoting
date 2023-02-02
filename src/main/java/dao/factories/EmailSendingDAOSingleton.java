package dao.factories;

import dao.api.IEmailSendingDAO;
import dao.database.EmailSendingDBDAO;
import dao.memory.EmailSendingMemoryDAO;

public class EmailSendingDAOSingleton {

    private static volatile IEmailSendingDAO instance;

    private EmailSendingDAOSingleton() {
    }

    public static IEmailSendingDAO getInstance(DAOType type) {
        if (instance == null) {
            synchronized (EmailSendingDAOSingleton.class) {
                if (instance == null) {
                    switch (type) {
                        case DB: {
                            instance = new EmailSendingDBDAO();
                            break;
                        }
                        case MEMORY: {
                            instance = new EmailSendingMemoryDAO();
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Illegal DAO type provided");
                        }
                    }
                }
            }
        }
        return instance;
    }
}