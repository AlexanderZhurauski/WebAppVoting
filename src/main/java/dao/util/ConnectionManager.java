package dao.util;

import dao.api.IConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class ConnectionManager implements IConnection {

    EntityManager entityManager;

    public ConnectionManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("voting");
        entityManager = factory.createEntityManager();
    }

    @Override
    public EntityManager open() {
        return entityManager;
    }

    @Override
    public void close() {

    }
}