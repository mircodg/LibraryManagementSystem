package com.library.management.menu;

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

    public ClientMenu(Socket socket, Connection connection) {
        this.socket = socket;
        this.userRepository = new UserRepository(connection);
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            System.err.println("error while creating ClientMenu: " + e.getMessage());
        }
    }

//    public ClientMenu(Socket socket) {
//        this.socket = socket;
//        try {
//            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//        } catch (IOException e) {
//            System.err.println("error while creating ClientMenu: " + e.getMessage());
//        }
//    }

    // registration and login menu
    public void displayMenu() throws IOException {
        boolean loop = true;
        out.println("=== Welcome to the Library === ");
        out.println("1. Login");
        out.println("2. Register");

        while (loop) {
            int choice = Integer.parseInt(in.readLine());
            switch (choice) {
                case 1:
                    login();
                    loop = false;
                    break;
                case 2:
                    register();
                    loop = false;
                    break;
                default:
                    out.println("Invalid choice");
            }
        }
    }

    public void login() throws IOException {
        out.println("Enter username: ");
        String username = in.readLine();
        out.println("Enter password: ");
        String password = in.readLine();
        User user = userRepository.getUserByCredentials(username, password);
        if (user != null) {
            out.println("Successfully logged in");
            if (user.getRole().equals("admin")) {
                // AdminMenu.displayMenu
            } else {
                UserMenu myUserMenu = new UserMenu(this.socket, userRepository.getConnection(), user);
                myUserMenu.displayMenu();
            }
        } else {
            out.println("Invalid username or password");
            displayMenu();
        }
    }

    public void register() throws IOException {
        out.println("Enter username: ");
        String username = in.readLine();
        out.println("Enter password: ");
        String password = in.readLine();
        User user = userRepository.getUserByCredentials(username, password);
        System.out.println(user);
        // if username is valid (not used)
        if (user == null) {
            userRepository.addUser(username, password);
            // no admin can be register so redirect to user menu
            out.println("Successfully registered");
            // retrieve user
            User newUser = userRepository.getUserByCredentials(username, password);
            UserMenu myUserMenu = new UserMenu(this.socket, userRepository.getConnection(), newUser);
            myUserMenu.displayMenu();
        } else {
            out.println("User already exists");
            out.println("/clear");
        }
    }
}
