package com.library.management.client;

import java.io.*;
import java.net.Socket;


public class ClientHandler extends Thread {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            System.err.println("[SERVER] error while allocating socket for the client: " + e);
            this.closeAll();
        }
    }

    public void closeAll(){
        try{
            if(socket != null && in != null && out != null){
                socket.close();
                in.close();
                out.close();
                System.out.println("[SERVER] client socket closed");
            }
        } catch (IOException e) {
            System.err.println("[SERVER] error while closing the socket for the client: " + e);
        }
    }

    public void listen(){
        try {
            String request;
            while (!(request = in.readLine()).equals("[CLIENT] /exit")) {
                System.out.println(request);
            }
            System.out.println("[SERVER] client socket closed");
            this.closeAll();
        } catch (IOException e) {
            System.err.println("[SERVER] error while reading socket for the client: " + e);
            this.closeAll();
        }
    }

    @Override
    public void run() {
        this.listen();
    }
}
