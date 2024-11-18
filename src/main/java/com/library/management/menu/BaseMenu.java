package com.library.management.menu;

import com.library.management.database.BookRepository;
import com.library.management.database.UserRepository;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;


public abstract class BaseMenu {

    protected Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    protected UserRepository userRepository;
    protected BookRepository bookRepository;
    protected Connection connection;

    public BaseMenu(Socket socket, Connection connection) throws IOException {
        this.socket = socket;
        this.connection = connection;
        this.userRepository = new UserRepository(connection);
        this.bookRepository = new BookRepository(connection);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    protected void clearScreen() {
        out.println("/clear");
    }

    protected void stopClient() {
        out.println("/exit");
    }

    protected abstract void showMenuOptions();

    protected abstract void handleMenuOptions() throws IOException;

    protected void handleError(Exception e) {
        out.println("An error occurred: " + e.getMessage());
        out.println("Press enter to continue...");
        try {
            in.readLine();
        } catch (IOException ignored) {
        }
    }

    public final void displayMenu() {
        clearScreen();
        showMenuOptions();
        try {
            handleMenuOptions();
        } catch (Exception e) {
            handleError(e);
        }
    }

    protected String readInput() throws IOException {
        return in.readLine();
    }

    protected void waitAndReturn() throws IOException {
        out.println();
        out.println("Enter anything to go back ");
        readInput();
    }

    protected void closeAll() {
        try {
            System.out.println("[SERVER] clearing resources and stopping client thread");
            if(!socket.isClosed()){
                socket.close();
            }
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
