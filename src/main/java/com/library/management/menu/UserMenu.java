package com.library.management.menu;

import com.library.management.books.Book;
import com.library.management.users.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;


public class UserMenu extends BaseMenu {
    User myUser;

    public UserMenu(Socket socket, Connection connection, String username, String password) throws IOException {
        super(socket, connection);
        myUser = new User(username, password);
        updateUserLocally(myUser);
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

    private void completeRegistration() throws IOException {
        clearScreen();
        out.println("Complete your registration");
        out.println("First name: ");
        String firstName = readInput();
        out.println("Last name: ");
        String lastName = readInput();
        boolean response = userRepository.updateUser(myUser, firstName, lastName);
        boolean localUpdate = updateUserLocally(myUser);
        if (response && localUpdate) {
            out.println("Thank you, you're information have been saved");
            waitAndReturn();
        }
    }

    private void showRentedBooks() throws IOException {
        List<Book> rentedBooks = bookRepository.getUserBooks(myUser.getUserID());
        if (rentedBooks.isEmpty()) {
            out.println();
            out.println("No books rented yet");
        }
        for (Book book : rentedBooks) {
            out.println();
            out.println("Id: " + book.getId());
            out.println("Title: " + book.getTitle());
        }
        waitAndReturn();
    }

    private void rentBook() throws IOException {
        List<Book> bookToRent = bookRepository.getAvailableBooks();

        if (bookToRent.isEmpty()) {
            out.println();
            out.println("No books to rent");
        } else {
            out.println();
            out.println("All books available to rent: " + bookToRent.size());
            out.println();
            for (Book book : bookToRent) {
                out.println("Id: " + book.getId() + " | Title: " + book.getTitle() + " | Author: " + book.getAuthor());
                out.println();
            }
            out.println("Enter the bookID of the book you want to rent: ");
            try {
                int bookID = Integer.parseInt(readInput());
                boolean response = bookRepository.rentBook(bookID, myUser.getUserID());
                if (response) {
                    out.println();
                    out.println("Book Rented");
                } else {
                    out.println();
                    out.println("You can't rent this book");
                }
            } catch (NumberFormatException e) {
                out.println();
                out.println("bookID not valid");
            } finally {
                waitAndReturn();
                clearScreen();
            }
        }
    }

    private void returnBook() throws IOException {
        out.println();
        out.println("enter bookID: ");
        try {
            int bookID = Integer.parseInt(readInput());
            boolean response = bookRepository.returnBook(myUser.getUserID(), bookID);
            if (response) {
                out.println();
                out.println("Thank you for returning the book");
            } else {
                out.println();
                out.println("bookID not valid");
            }
        } catch (NumberFormatException e) {
            out.println();
            out.println("bookID not valid");
        } finally {
            waitAndReturn();
            clearScreen();
        }
    }

    @Override
    public void showMenuOptions() {
        if (myUser.getFirstName() == null) {
            try {
                completeRegistration();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        clearScreen();
        out.println(myUser.getFirstName().toUpperCase() + "'s Library");
        out.println("1. See rented books");
        out.println("2. Rent a books");
        out.println("3. Return a book");
        out.println("4. Logout");
    }

    @Override
    protected void handleMenuOptions() throws IOException {
        while (true) {
            String choice = readInput();
            switch (choice) {
                case "1":
                    showRentedBooks();
                    showMenuOptions();
                    break;
                case "2":
                    rentBook();
                    showMenuOptions();
                    break;
                case "3":
                    returnBook();
                    showMenuOptions();
                    break;
                case "4":
                    clearScreen();
                    return;
                default:
                    break;
            }
        }
    }
}
