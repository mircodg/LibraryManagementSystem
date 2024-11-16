package com.library.management.server;

import com.library.management.client.ClientHandler;
import com.library.management.database.DatabaseConnection;
import com.library.management.utils.ConfigParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class LibraryServer {
    private ServerSocket serverSocket;
    Connection connection; // single connection with the database shared among client threads.
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();


    public LibraryServer() {
        ConfigParser configParser = new ConfigParser();
        try {
            this.serverSocket = new ServerSocket(configParser.getServerPort());
            System.out.println("[SERVER] port allocated");
        } catch (IOException e) {
            System.err.println("[SERVER] could not use port: " + configParser.getServerPort());
            System.exit(1);
        }
    }

    public void start() {
        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            this.connection = databaseConnection.getConnection();
            System.out.println("[SERVER] connected to database ");
            System.out.println("[SERVER] listening on port " + serverSocket.getLocalPort());
            while (!serverSocket.isClosed()) {
                // listening for clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] new client connected");
                // starting a new thread
//                clientHandler = new ClientHandler(clientSocket, this.databaseConnection);
                ClientHandler clientHandler = new ClientHandler(clientSocket, connection);
                this.clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("[SERVER] could not accept connection");
            this.stop();
        }catch (SQLException e) {
            System.err.println("[SERVER] could not connect to database");
            System.err.println("[SERVER] " + e.getMessage());
            this.stop();
        }
    }

    public void stop() {
        try {
            if (this.serverSocket != null) {
                // closing every client socket still open
                for (ClientHandler clientHandler : this.clientHandlers) {
                    if (clientHandler.getSocket() != null) {
                        clientHandler.closeAll();
                    }
                }
                // closing server socket and db connection
                if (this.connection != null) {
                    connection.close();
                }
                if (this.serverSocket != null) {
                    this.serverSocket.close();
                }
                System.out.println("[SERVER] server socket and db connection closed");
                // exit
                System.exit(0);
            }
            System.out.println("[SERVER] exiting");
        } catch (IOException e) {
            System.err.println("[SERVER] could not close server socket " + e.getMessage());
        }catch(SQLException sqlException){
            System.err.println("[SERVER] could not close database connection " + sqlException.getMessage());
        }
    }


    public static void main(String[] args) {
        LibraryServer server = new LibraryServer();
        server.start();
    }

}
