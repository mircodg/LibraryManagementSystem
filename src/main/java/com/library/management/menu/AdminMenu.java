package com.library.management.menu;

import com.library.management.users.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

public class AdminMenu extends ClientMenu{

    public AdminMenu(User user, Socket socket, Connection connection) {
        super(socket, connection);
    }

    @Override
    public void displayMenu(){
        out.println("Management System Admin Menu");
        out.println("1. Add Book\n2. Delete Book\n3. Edit Book\n4. See Users\n5. Delete User\n6.Inspect User\n7. Exit");
        boolean loop = true;
        while(loop){
            try{
                int choice = Integer.parseInt(in.readLine());
                switch (choice){
                    case 1:
                        System.out.println("Add Book");
                        displayMenu();
                        break;
                    case 2:
                        System.out.println("Delete Book");
                        displayMenu();
                        break;
                    case 3:
                        System.out.println("Edit Book");
                        displayMenu();
                        break;
                    case 4:
                        System.out.println("See Users");
                        displayMenu();
                        break;
                    case 5:
                        System.out.println("Delete User");
                        displayMenu();
                        break;
                    case 6:
                        System.out.println("Inspect User");
                        displayMenu();
                        break;
                    case 7:
                        System.out.println("exiting...");
                        loop = false;
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }catch(IOException e){
                System.err.println("Error while reading from input. " + e.getMessage());
                loop = false;
            }
        }
    }

    private void addBookMenu(){}
    private void deleteBookMenu(){}
    private void editBookMenu(){}
    private void seeUsersMenu(){}
    private boolean deleteUser(){return false;}
}
