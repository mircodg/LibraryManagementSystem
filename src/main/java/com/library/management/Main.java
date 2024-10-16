package com.library.management;
import com.library.management.database.DatabaseConnection;
import com.library.management.server.LibraryServer;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.connect();
        System.out.println("Database connection established");
        LibraryServer server = new LibraryServer();
        server.start();
    }
}