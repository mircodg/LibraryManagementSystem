package com.library.management;
import com.library.management.database.DatabaseConnection;
import com.library.management.server.LibraryServer;

public class Main {
    public static void main(String[] args) {
        LibraryServer server = new LibraryServer();
        server.start();
    }
}