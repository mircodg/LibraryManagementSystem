package com.library.management.client;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
    }
}
