package com.library.management.client;

import com.library.management.utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
        } catch (IOException e) {
            System.err.println("[CLIENT] error while creating socket");
            this.closeAll();
        }
    }

    public void closeAll() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            System.exit(0);
        } catch (IOException e) {
            System.err.println("[CLIENT] error while closing connection");
        }
    }

    public void sendMessage() {
        Scanner scanner = new Scanner(System.in);
        while (!socket.isClosed()) {
            String messageToSend = scanner.nextLine();
            out.println(messageToSend);
            if (messageToSend.equals("/exit")) {
                this.closeAll();
                System.out.println("[CLIENT] closed socket and exiting");
            }
        }
    }

//    public void listen() throws IOException {
//        new Thread(new Runnable() {
//            @Override
//            public void run(){
//                while (!socket.isClosed()) {
//                    String receivedMessage = "";
//                    try {
//                        receivedMessage = in.readLine();
//                    } catch (IOException e) {
//                        closeAll();
//                    }
//                    if (receivedMessage.equals("/clear")) {
//                        Utils.clearScreen();
//                    } else  if (receivedMessage.equals("null")){
//                        System.out.println("Server closed the connection");
//                        closeAll();
//                    }
//                    else{
//                        System.out.println(receivedMessage);
//                    }
//                }
//            }
//        }).start();
//    }
        public void listen() throws IOException {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!socket.isClosed()) {
                        String receivedMessage;
                        try {
                            receivedMessage = in.readLine();
                            if (receivedMessage == null) {
                                System.out.println("Server closed the connection");
                                closeAll();
                                break;
                            }
                        } catch (SocketException e) {
                            System.out.println("SocketException: Server might have closed the connection");
                            closeAll();
                            break;
                        } catch (IOException e) {
                            System.out.println("IOException occurred: " + e.getMessage());
                            closeAll();
                            break;
                        }
                        if ("/clear".equals(receivedMessage)) {
                            Utils.clearScreen();
                        }else if("/exit".equals(receivedMessage)) {
                            closeAll();
                            System.exit(0);
                        } else {
                            System.out.println(receivedMessage);
                        }
                    }
                }
            }).start();
        }


    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8000);
            Client client = new Client(socket);
            client.listen();
            client.sendMessage();
        } catch (IOException e) {
            System.err.println("[CLIENT] server not available");
        }
    }

}
