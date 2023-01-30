package dao.util;

import dao.api.IConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class ConnectionManager implements IConnection {

    private final EntityManagerFactory factory;
    public static final String PERSISTENT_UNIT_NAME = "voting";

    public ConnectionManager() {
        factory = Persistence.createEntityManagerFactory(PERSISTENT_UNIT_NAME);
    }

    @Override
    public EntityManager open() {
        return factory.createEntityManager();
    }

    @Override
    public void close() {
        factory.close();
    }
}