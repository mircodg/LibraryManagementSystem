package com.library.management.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.library.management.utils.ConfigParser;

public class DatabaseConnection {
    private Connection conn;

    public DatabaseConnection() throws SQLException {
        connect();
    }

    public void connect() throws  SQLException {
        ConfigParser config = new ConfigParser();
        conn = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPass());
    }

}
