package com.library.management.menu;

import com.library.management.books.Book;
import com.library.management.users.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;


public class AdminMenu extends BaseMenu {

    public AdminMenu(Socket socket, Connection connection) throws IOException {
        super(socket, connection);
    }

    private void addBook() throws IOException {
        clearScreen();
        try {
            out.println("== Add Book ==");
            out.println();
            out.println("title: ");
            String title = readInput();
            out.println("author: ");
            String author = readInput();
            out.println("description: ");
            String description = readInput();
            out.println("publisher: ");
            String publisher = readInput();
            out.println("published in: ");
            String published = readInput();
            Book newBook = new Book(title, author, description, publisher, published);
            boolean response = bookRepository.addBook(newBook);
            if (response) {
                out.println("Book added successfully");
            } else {
                out.println("Error while adding new book");
            }
        } catch (IllegalArgumentException e) {
            out.println("Date must be in this format: YYYY-MM-DD");
        } finally {
            waitAndReturn();
            clearScreen();
        }
    }

    private void deleteBook() throws IOException {
        clearScreen();
        out.println("== Delete Book ==");
        out.println();
        out.println("Enter the ID of the book to be deleted: ");
        try {
            int id = Integer.parseInt(readInput());
            boolean response = bookRepository.deleteBook(id);
            if (response) {
                out.println("Book deleted successfully");
            } else {
                out.println("Book not found");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid bookID");
        } finally {
            waitAndReturn();
            clearScreen();
        }
    }

    private void showBooks() throws IOException {
        clearScreen();
        out.println("== Library == ");
        out.println();
        List<Book> bookList = bookRepository.getAllBooks();
        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                out.println("Title: " + book.getTitle() + " | bookID: " + book.getId());
            }
        } else {
            out.println("No books found");
        }
        waitAndReturn();
        clearScreen();
    }

    private void displayBookInfo(Book book) {
        out.println("Id: " + book.getId());
        out.println("Title: " + book.getTitle());
        out.println("Author: " + book.getAuthor());
        out.println("Description: " + book.getDescription());
        out.println("publisher: " + book.getPublisher());
        out.println("published in: " + book.getPublishedDate());
    }

    private void searchBookByTitle() throws IOException {
        clearScreen();
        out.println("== Search Book By Title ==");
        out.println();
        out.println("Enter the title: ");
        String title = readInput();
        Book book = bookRepository.getBookByTitle(title);
        if (book != null)
            displayBookInfo(book);
        else
            out.println("Book not found");
        waitAndReturn();
        clearScreen();
    }

    private void searchBookById() throws IOException {
        clearScreen();
        out.println("== Search Book By bookID ==");
        out.println();
        out.println("Enter the ID of the book: ");
        try {
            int id = Integer.parseInt(readInput());
            Book book = bookRepository.getBookById(id);
            if (book != null) {
                out.println();
                displayBookInfo(book);
            }
            else
                out.println("Book not found");
        } catch (NumberFormatException e) {
            out.println("Invalid bookID");
        } finally {
            waitAndReturn();
            clearScreen();
        }
    }

    private void displayUser(User user) {
        out.println("ID: " + user.getUserID());
        out.println("Username: " + user.getUsername());
        out.println("First Name: " + user.getFirstName());
        out.println("Last Name: " + user.getLastName());
        List<Book> bookList = bookRepository.getUserBooks(user.getUserID());
        out.println("Books rented: ");
        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                out.println("Title: " + book.getTitle());
            }
        } else {
            out.println("No books rented yet");
        }
    }

    private void getUsersList() throws IOException {
        clearScreen();
        out.println("== User List ==");
        out.println();
        List<User> userList = userRepository.getUsers();
        if (userList.isEmpty())
            out.println("No users found");
        else{
            for (User user : userList) {
                displayUser(user);
                out.println();
            }
        }
        waitAndReturn();
        clearScreen();
    }

    private void getUserById() throws IOException {
        clearScreen();
        out.println("== Get user by userID ==");
        out.println();
        out.println("Enter the ID of the user: ");
        try{
            int id = Integer.parseInt(readInput());
            User user = userRepository.getUser(id);
            if(user != null) {
                out.println();
                displayUser(user);
            }
            else
                out.println("User not found");
        } catch (NumberFormatException e) {
            out.println("Invalid userID");
        } finally {
            waitAndReturn();
            clearScreen();
        }
    }

    private void getUserByUsername() throws IOException {
        clearScreen();
        out.println("== Get user by username ==");
        out.println();
        out.println("Enter the user's username: ");
        String username = readInput();
        User user = userRepository.getUser(username);
        if (user != null) {
            out.println();
            displayUser(user);
        }
        else
            out.println("User not found");
        waitAndReturn();
        clearScreen();
    }

    private void deleteUserByUserId() throws IOException {
        clearScreen();
        out.println("== Delete user by userID ==");
        out.println();
        out.println("Enter the ID of the user: ");
        try{
            int id = Integer.parseInt(readInput());
            if(id != 1){
                boolean isDeleted = userRepository.deleteUser(id);
                if (isDeleted)
                        out.println("User deleted successfully");
                else
                    out.println("User not found");
            }else
                out.println("You can't delete yourself");
        } catch (NumberFormatException e) {
            out.println("Invalid userID");
        } finally {
            waitAndReturn();
            clearScreen();
        }
    }

    private void deleteUserByUsername() throws IOException {
        clearScreen();
        out.println("== Delete user by username ==");
        out.println();
        out.println("Enter user's username: ");
        String username = readInput();
        if(!username.equals("admin")){
            boolean isDeleted = userRepository.deleteUser(username);
            if (isDeleted)
                out.println("User deleted successfully");
            else
                out.println("User not found");
        }else
            out.println("You can't delete yourself");
        waitAndReturn();
        clearScreen();
    }


    private void showBookOptions() {
        clearScreen();
        out.println("== Show Book Options ==");
        out.println();
        out.println("1. Search by title");
        out.println("2. Search by bookID");
        out.println("3. Go back to main menu");
        out.println("Enter your choice: ");
    }

    private void handleBookOptions() throws IOException {
        while (true) {
            String choice = readInput();
            switch (choice) {
                case "1":
                    searchBookByTitle();
                    showBookOptions();
                    break;
                case "2":
                    searchBookById();
                    showBookOptions();
                    break;
                case "3":
                    clearScreen();
                    return;
                default:
                    clearScreen();
                    showBookOptions();
                    break;
            }
        }
    }


    private void manageUsersMenu() {
        clearScreen();
        out.println("== Users management menu ==");
        out.println();
        out.println("1. See user list");
        out.println("2. See user info by userID");
        out.println("3. See user info by username");
        out.println("4. Delete user by userID");
        out.println("5. Delete user by username");
        out.println("6. Go back to admin menu");
        out.println("Enter your choice: ");
    }

    private void handleManageUsersMenu() throws IOException {
        while (true) {
            String choice = readInput();
            switch (choice) {
                case "1":
                    getUsersList();
                    manageUsersMenu();
                    break;
                case "2":
                    getUserById();
                    manageUsersMenu();
                    break;
                case "3":
                    getUserByUsername();
                    manageUsersMenu();
                    break;
                case "4":
                    deleteUserByUserId();
                    manageUsersMenu();
                    break;
                case "5":
                    deleteUserByUsername();
                    manageUsersMenu();
                    break;
                case "6":
                    clearScreen();
                    return;
                default:
                    clearScreen();
                    manageUsersMenu();
                    break;
            }
        }
    }

    @Override
    public void showMenuOptions() {
        out.println("== Management System Admin Menu ==");
        out.println();
        out.println("1. Add Book");
        out.println("2. Delete Book");
        out.println("3. View Books");
        out.println("4. View Book");
        out.println("5. Manage Users");
        out.println("6. Logout");
        out.println("Enter your choice: ");
    }


    @Override
    protected void handleMenuOptions() throws IOException {
        while (true) {
            String choice = readInput();
            switch (choice) {
                case "1":
                    addBook();
                    showMenuOptions();
                    break;
                case "2":
                    deleteBook();
                    showMenuOptions();
                    break;
                case "3":
                    showBooks();
                    showMenuOptions();
                    break;
                case "4":
                    showBookOptions();
                    handleBookOptions();
                    showMenuOptions();
                    break;
                case "5":
                    manageUsersMenu();
                    handleManageUsersMenu();
                    showMenuOptions();
                    break;
                case "6":
                    clearScreen();
                    return;
                default:
                    clearScreen();
                    showMenuOptions();
                    break;
            }
        }
    }
}
