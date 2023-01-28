package dao.api;

import javax.persistence.EntityManager;
import java.sql.SQLException;

public interface IConnection extends AutoCloseable{
    EntityManager open();
}
