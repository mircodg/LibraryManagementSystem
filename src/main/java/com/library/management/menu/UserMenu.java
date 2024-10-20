package com.library.management.menu;

import com.library.management.database.UserRepository;
import com.library.management.users.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

public class UserMenu extends ClientMenu{
    User myUser;
    UserRepository userRepository;

    public UserMenu(Socket socket, User user) {
        super(socket);
        this.myUser = user;
    }



    @Override
    public void displayMenu() {
        System.out.println("AOAOAOAOAO");
        if(myUser.getFirstName() == null){
            // using attributes of father class
            try{
                out.println("Let me know more about yourself");
                out.println("Enter your first name: ");
                String firstName = in.readLine();
                out.println("Enter your last name");
                String lastName = in.readLine();
                userRepository.updateUser(myUser.getUserID(), myUser.getFirstName(), myUser.getLastName(), firstName, lastName);
            }catch(IOException e){
                System.err.println("error while reading your input: " + e.getMessage());
            }
        }else{
            out.println("Welcome back " + myUser.getFirstName());
        }
    }

}
