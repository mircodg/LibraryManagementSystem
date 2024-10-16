package com.library.management.server;

import com.library.management.client.ClientHandler;
import com.library.management.utils.ConfigParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LibraryServer{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ConfigParser configParser;
    private boolean running = true;
    private int activeClients;

    public void start() {
        this.configParser = new ConfigParser();
        this.activeClients = 0;
        ClientHandler clientHandler;
        try {
            this.serverSocket = new ServerSocket(configParser.getServerPort());
            System.out.println("Server started on port " + configParser.getServerPort());
            while(this.running) {
                clientSocket = serverSocket.accept();
                this.activeClients++;
                clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Server could not start on port " + configParser.getServerPort());
            this.running = false;
            throw new RuntimeException(e.getMessage());
        }
    }

}
