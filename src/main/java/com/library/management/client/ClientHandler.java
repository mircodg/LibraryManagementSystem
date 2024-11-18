package com.library.management.client;
import com.library.management.menu.ClientMenu;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;


public class ClientHandler extends Thread {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    Connection connection; // shared db connection. May use a thread pool
    // so that we have multiple connections open only when necessary.


    public ClientHandler(Socket socket, Connection connection) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            this.connection = connection;
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

    public Socket getSocket(){
        return socket;
    }

//    public void listen(){
//        try {
//            String request;
//            while (!(request = in.readLine()).equals("/exit") || ((request = in.readLine()) != null)) {
//                System.out.println(request);
//            }
//            System.out.println("[SERVER] client socket closed");
//            this.closeAll();
//        } catch (IOException e) {
//            System.err.println("[SERVER] error while reading socket for the client: " + e);
//            this.closeAll();
//        }
//    }

    @Override
    public void run() {
        System.out.println("[SERVER] client thread started");
        try {
            ClientMenu clientMenu = new ClientMenu(this.connection, this.socket);
            clientMenu.displayMenu();
        } catch (IOException | NullPointerException e) {
            this.closeAll();
        }
    }
}
