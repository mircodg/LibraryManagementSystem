package com.library.management.menu;

import com.library.management.users.User;

import java.io.IOException;
import java.sql.Connection;
import java.net.Socket;

public class ClientMenu extends BaseMenu {

    public ClientMenu(Connection connection, Socket socket) throws IOException {
        super(socket, connection);
    }

    @Override
    protected void showMenuOptions() {
        out.println("=== Welcome to the Library === ");
        out.println("1. Login");
        out.println("2. Register");
        out.println("3. Exit");
        out.println("Choose your option: ");
    }

    @Override
    protected void handleMenuOptions() throws IOException {
        while (true) {
            String choice = readInput();
            switch (choice) {
                case "1":
                    login();
                    showMenuOptions();
                    break;
                case "2":
                    register();
                    showMenuOptions();
                    break;
                case "3":
                    out.println();
                    out.println("Thank you for using the Library, see you soon!");
                    stopClient();
                    closeAll();
                    System.out.println("[SERVER] client thread stopped");
                    return;
                default:
                    out.println("Invalid choice");
                    waitAndReturn();
                    clearScreen();
                    showMenuOptions();
                    break;
            }
        }
    }

    private void login() throws IOException, NullPointerException {
        out.println();
        out.println("Enter username: ");
        String username = readInput();
        out.println("Enter password: ");
        String password = readInput();
        User user = userRepository.getUserByCredentials(username, password);
        if (user != null) {
            out.println("Successfully logged in");
            BaseMenu nextMenu = user.getRole().equals("admin")
                    ? new AdminMenu(socket, connection)
                    : new UserMenu(socket, connection, username, password);
            nextMenu.displayMenu();
        } else {
            out.println("Invalid username or password");
            waitAndReturn();
            clearScreen();
        }
    }

    private void register() throws IOException {
        out.println();
        out.println("Enter username: ");
        String username = readInput();
        out.println("Enter password: ");
        String password = readInput();
        boolean isUsernameValid = userRepository.isUsernameValid(username);
        // if username is valid (not used)
        if (isUsernameValid) {
            boolean response = userRepository.addUser(username, password);
            if (response) {
                out.println("Successfully registered");
                new UserMenu(this.socket, connection, username, password).displayMenu();
            } else {
                out.println("error while registering user");
                waitAndReturn();
            }
        } else {
            out.println("username already taken by another user");
            waitAndReturn();
        }
    }
}
