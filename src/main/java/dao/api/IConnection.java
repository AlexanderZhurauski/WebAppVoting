package dao.api;

import javax.persistence.EntityManager;

public interface IConnection extends AutoCloseable{
    EntityManager open();
}
