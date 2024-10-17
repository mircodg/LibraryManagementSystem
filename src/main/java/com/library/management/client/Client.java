package com.library.management.client;

import java.io.*;
import java.net.Socket;

public class Client extends  Thread{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter((new OutputStreamWriter(this.socket.getOutputStream()))));
        } catch (IOException e) {
            System.err.println("[CLIENT] error while creating socket");
        }
    }
}
