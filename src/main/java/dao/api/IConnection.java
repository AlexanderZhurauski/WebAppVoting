package dao.api;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnection extends AutoCloseable {
    Connection open() throws SQLException;
}
