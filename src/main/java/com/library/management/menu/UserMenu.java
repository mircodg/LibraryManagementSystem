package com.library.management.menu;

import com.library.management.books.Book;
import com.library.management.users.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;

public class UserMenu extends ClientMenu {
    User myUser;

    public UserMenu(Socket socket, Connection connection, String username, String password) throws IOException {
        super(socket, connection);
        myUser = new User(username, password);
        updateUserLocally(myUser);
    }


    @Override
    public void displayMenu() {
        if (myUser.getFirstName() == null) {
            // using attributes of father class
            try {
                out.println("/clear");
                out.println("Let me know more about yourself");
                out.println("Enter your first name: ");
                String firstName = in.readLine();
                out.println("Enter your last name");
                String lastName = in.readLine();
                boolean localUpdate = updateUserLocally(myUser);
                boolean response = userRepository.updateUser(myUser, firstName, lastName);
                if (response && localUpdate) {
                    this.myUser = userRepository.getUserByCredentials(this.myUser.getUsername(), this.myUser.getPassword());
                    out.println("Thank you, you're information have been saved");
                    out.println("enter anything to go to your library");
                    in.readLine();
                    userActions();
                } else {
                    out.println("Something went wrong while saving the user");
                }
            } catch (IOException e) {
                System.err.println("error while reading your input: " + e.getMessage());
            }
        } else {
            // clear screen and show other menu
//            out.println("/clear");
            userActions();
        }
    }

    private void userActions() {
        out.println("/clear");
        out.println(myUser.getFirstName().toUpperCase() + "'s Library");
        out.println("1. See rented books\n2. Rent a book\n3. Return a book\n4. Exit\nChoice: ");
//        out.flush(); // this because out.print() don't get automatically flushed.
        try {
            int choice = Integer.parseInt(in.readLine());
            switch (choice) {
                case 1:
                    showRentedBooks();
                    break;
                case 2:
                    rentBookMenu();
                    break;
                case 3:
                    returnBookMenu();
                    break;
                case 4:
                    out.println("Thank you for using our system, see you soon !");
                    out.println("/exit");
                    break;
            }

        } catch (IOException e) {
            System.err.println("error while reading your input: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Please enter a valid number");
            userActions();
        }

    }

    private void showRentedBooks() throws IOException {
        List<Book> rentedBooks = bookRepository.getUserBooks(myUser.getUserID());
        if (rentedBooks.isEmpty()) {
            out.println("No books rented yet");
        }
        for (Book book : rentedBooks) {
            out.println("Id: " + book.getId());
            out.println("Title: " + book.getTitle());
        }
        out.println("Enter anything to go back to your library");
        in.readLine();
        userActions();
    }

    private void rentBookMenu() {
        List<Book> bookToRent = bookRepository.getAvailableBooks();
        try {
            if (bookToRent.isEmpty()) {
                out.println("No books to rent");
                out.println("Enter anything to go back to your library");
                in.readLine();
                userActions();
            } else {
                out.println("All books available to rent: " + bookToRent.size());
                for (Book book : bookToRent) {
                    out.println("Id: " + book.getId() + " Title: " + book.getTitle() + " Author: " + book.getAuthor());
                }
                out.println("Enter the bookID of the book you want to rent: ");
                int bookID = Integer.parseInt(in.readLine());
                boolean response = bookRepository.rentBook(bookID, myUser.getUserID());
                if (response) {
                    out.println("Book Rented");
                } else {
                    out.println("You can't rent this book");
                }
                out.println("Enter anything to go back to your library");
                in.readLine();
                userActions();
            }
        } catch (IOException e) {
            System.err.println("error while reading your input: " + e.getMessage());
            rentBookMenu();
        } catch (NumberFormatException e) {
            out.println("Please enter a valid number");
            rentBookMenu();
        }
    }

    private void returnBookMenu() {
        out.println("enter bookID: ");
        try {
            int bookID = Integer.parseInt(in.readLine());
            boolean response = bookRepository.returnBook(myUser.getUserID(), bookID);
            if (response) {
                out.println("Thank you for returning the book");
            } else {
                out.println("bookID not valid");
            }
            out.println("Enter anything to go back to your library");
            in.readLine();
            userActions();
        } catch (NumberFormatException n) {
            System.err.println("you entered an invalid number enter a valid number");
            returnBookMenu();
        } catch (IOException e) {
            System.err.println("error while reading your input: " + e.getMessage());
            returnBookMenu();
        }
    }

    private boolean updateUserLocally(User user) {
        User userInfo = userRepository.getUserByCredentials(user.getUsername(), user.getPassword());
        if (userInfo != null) {
            user.setFirstName(userInfo.getFirstName());
            user.setLastName(userInfo.getLastName());
            user.setUserID(userInfo.getUserID());
            return true;
        } else {
            return false;
        }
    }

    // TODO: exit logic

}
