package com.library.management;
import com.library.management.database.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.connect();
    }
}