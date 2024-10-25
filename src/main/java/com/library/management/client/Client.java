package com.library.management.client;

import com.library.management.utils.Utils;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean running = true;

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
            System.out.println("[CLIENT] connection closed, exiting...");
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

    public void listen() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run(){
                while (!socket.isClosed()) {
                    String receivedMessage = null;
                    try {
                        receivedMessage = in.readLine();
                    } catch (IOException e) {
                        closeAll();
                    }
                    assert receivedMessage != null;
                    if (receivedMessage.equals("/clear")) {
                        Utils.clearScreen();
                    } else {
                        System.out.println(receivedMessage);
                    }
                }
            }
        }).start();
    }

public static void main(String[] args) {
    Client client;
    try {
        Socket socket = new Socket("localhost", 8000);
        client = new Client(socket);
        client.listen();
        client.sendMessage();
    } catch (IOException e) {
        System.err.println("[CLIENT] server not available");
    }
}

}
