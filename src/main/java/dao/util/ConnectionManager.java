package dao.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import dao.api.IConnection;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager implements IConnection {
    private final String URL = "db.url";
    private final String USERNAME = "db.username";
    private final String PASSWORD = "db.password";
    private final String DRIVER = "db.driver";
    private final ComboPooledDataSource cpds;

    public ConnectionManager() {
        cpds = new ComboPooledDataSource();
        loadDriver();
    }

    private void loadDriver() {
        try {
            cpds.setDriverClass(PropertiesUtil.get(DRIVER));
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        cpds.setJdbcUrl(PropertiesUtil.get(URL));
        cpds.setUser(PropertiesUtil.get(USERNAME));
        cpds.setPassword(PropertiesUtil.get(PASSWORD));
    }

    @Override
    public Connection open() throws SQLException {
        return cpds.getConnection();
    }

    @Override
    public void close() throws Exception {
        cpds.close();
    }
}