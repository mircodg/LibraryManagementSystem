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

public class LibraryServer{
    private ServerSocket serverSocket;
    private Connection connection;
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();


    public LibraryServer() {
        ConfigParser configParser = new ConfigParser();
        try {
            this.serverSocket = new ServerSocket(configParser.getServerPort());
            System.out.println("[SERVER] port allocated");
        }catch(IOException e){
            System.err.println("[SERVER] could not use port: " + configParser.getServerPort());
        }
    }

    public void start() {
        ClientHandler clientHandler;
        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            this.connection = databaseConnection.getConnection();
            System.out.println("[SERVER] connected to database ");
            System.out.println("[SERVER] listening on port " + serverSocket.getLocalPort());
            while(!this.serverSocket.isClosed()) {
                // listening for clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] new client connected");
                // starting a new thread
//                clientHandler = new ClientHandler(clientSocket, this.databaseConnection);
                clientHandler = new ClientHandler(clientSocket, connection);
                this.clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("[SERVER] could not accept connection");
            this.stop();
            throw new RuntimeException(e.getMessage());
        }catch (SQLException e) {
            System.err.println("[SERVER] could not connect to database");
            this.stop();
        }
    }

    public void stop() {
        try {
            if(this.serverSocket != null) {
                // closing every client socket
                for(ClientHandler clientHandler : this.clientHandlers) {
                    clientHandler.closeAll();
                }
                // closing server socket
                this.serverSocket.close();
//                this.databaseConnection.disconnect();
                System.out.println("[SERVER] server socket and db connection closed");
            }
            System.out.println("[SERVER] exiting");
        } catch (IOException e) {
            System.err.println("[SERVER] could not close server socket");
        }
//        catch (SQLException e) {
//            System.err.println("[SERVER] error closing connection with database");
//        }
    }

}
