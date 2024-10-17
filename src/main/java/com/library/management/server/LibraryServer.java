package com.library.management.server;

import com.library.management.client.ClientHandler;
import com.library.management.database.DatabaseConnection;
import com.library.management.utils.ConfigParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class LibraryServer{
    private final ServerSocket serverSocket;
    private final DatabaseConnection databaseConnection;



    public LibraryServer() {
        ConfigParser configParser = new ConfigParser();
        this.databaseConnection = new DatabaseConnection();
        this.serverSocket = new ServerSocket();
    }

    public void start() {
        ClientHandler clientHandler;
        try {
            System.out.println("[SERVER] listening on port " + serverSocket.getLocalPort());
            while(!this.serverSocket.isClosed()) {
                // listening for clients
                Socket clientSocket = serverSocket.accept();
                // starting a new thread
                clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("[SERVER] could not accept connection");
            this.stop();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void stop() {
        try {
            // close server socket
            if(this.serverSocket != null) {
                this.serverSocket.close();
            }
            System.out.println("[SERVER] server socket closed");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
