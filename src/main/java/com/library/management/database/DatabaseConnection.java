package com.library.management.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.library.management.utils.ConfigParser;

public class DatabaseConnection {
    private Connection connection;

    public void connect() {
        ConfigParser config = new ConfigParser();
        try{
            connection = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPass());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
