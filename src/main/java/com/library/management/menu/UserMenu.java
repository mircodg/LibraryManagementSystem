package com.library.management.menu;

import com.library.management.database.UserRepository;
import com.library.management.users.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

public class UserMenu extends ClientMenu{
    User myUser;

    public UserMenu(Socket socket, Connection connection, User user) {
        super(socket, connection);
        this.myUser = user;
    }


    @Override
    public void displayMenu() {
        if(myUser.getFirstName() == null){
            // using attributes of father class
            try{
                out.println("Let me know more about yourself");
                out.println("Enter your first name: ");
                String firstName = in.readLine();
                out.println("Enter your last name");
                String lastName = in.readLine();
                boolean response = userRepository.updateUser(myUser, firstName, lastName);
                if(response){
                    this.myUser = userRepository.getUserByCredentials(this.myUser.getUsername(), this.myUser.getPassword());
                    out.println("Thank you, you're information have been saved");
//                    out.println("/clear");
                    userActions();
                }
            }catch(IOException e){
                System.err.println("error while reading your input: " + e.getMessage());
            }
        }else{
            // clear screen and show other menu
            out.println("/clear");
            userActions();
        }
    }

    private void userActions(){
        out.println(myUser.getFirstName().toUpperCase() + "'s Library");
        out.println("1. See rented books\n2. Rent a book\n3. Return a book\n4. Exit\nChoice: ");
        out.flush(); // this because out.print() don't get automatically flushed.
        boolean loop = true;
        try{
            while (loop){
                int choice = Integer.parseInt(in.readLine());
                switch(choice){
                    case 1:
                        showBooksMenu();
                        loop = false;
                        break;
                    case 2:
                        rentBookMenu();
                        loop = false;
                        break;
                    case 3:
                        returnBookMenu();
                        loop = false;
                        break;
                    case 4:
                        // exit logic
                        break;
                }
            }
        }catch(IOException e){
            System.err.println("error while reading your input: " + e.getMessage());
            loop = false;
        }

    }

    private void showBooksMenu(){}
    private void rentBookMenu(){}
    private void returnBookMenu(){}

    // TODO: exit logic

}
