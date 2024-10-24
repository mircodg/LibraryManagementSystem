package com.library.management.menu;

import com.library.management.database.BookRepository;
import com.library.management.database.UserRepository;
import com.library.management.users.User;


import java.io.*;
import java.net.Socket;
import java.sql.Connection;

public class ClientMenu {
    protected final Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    protected UserRepository userRepository;
    protected BookRepository bookRepository;

    public ClientMenu(Socket socket, Connection connection) {
        this.socket = socket;
        this.userRepository = new UserRepository(connection);
        this.bookRepository = new BookRepository(connection);
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            System.err.println("error while creating ClientMenu: " + e.getMessage());
        }
    }

    // registration and login menu
    public void displayMenu() throws IOException {
        out.println("=== Welcome to the Library === ");
        out.println("1. Login");
        out.println("2. Register");

        String choice = in.readLine();
        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            default:
                out.println("Invalid choice");
                displayMenu();
        }

    }

    private void login() throws IOException {
        out.println("Enter username: ");
        String username = in.readLine();
        out.println("Enter password: ");
        String password = in.readLine();
        User user = userRepository.getUserByCredentials(username, password);
        if (user != null) {
            out.println("Successfully logged in");
            if (user.getRole().equals("admin")) {
                AdminMenu myAdminMenu = new AdminMenu(this.socket, userRepository.getConnection());
                myAdminMenu.displayMenu();
            } else {
                UserMenu myUserMenu = new UserMenu(this.socket, userRepository.getConnection(), username, password);
                myUserMenu.displayMenu();
            }
        } else {
            out.println("Invalid username or password");
            displayMenu();
        }
    }

    private void register() throws IOException {
        out.println("Enter username: ");
        String username = in.readLine();
        out.println("Enter password: ");
        String password = in.readLine();
        boolean isUsernameValid = userRepository.isUsernameValid(username);
        // if username is valid (not used)
        if (isUsernameValid) {
            boolean response = userRepository.addUser(username, password);
            if(response){
                // no admin can be register so redirect to user menu
                out.println("Successfully registered");
                // retrieve user
                User newUser = userRepository.getUserByCredentials(username, password);
                UserMenu myUserMenu = new UserMenu(this.socket, userRepository.getConnection(), username, password);
                myUserMenu.displayMenu();
            }else {
                out.println("error while registering user");
                out.println("enter anything to go back to the menu");
                in.readLine();
                displayMenu();
            }

        } else {
            out.println("username already taken by another user");
            out.println("enter anything to go back to the menu");
            displayMenu();
            in.readLine();
            out.println("/clear");
        }
    }
}
