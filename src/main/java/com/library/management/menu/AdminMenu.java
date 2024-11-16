package com.library.management.menu;

import com.library.management.books.Book;
import com.library.management.users.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;

public class AdminMenu extends ClientMenu {

    public AdminMenu(Socket socket, Connection connection) throws IOException {
        super(socket, connection);
    }

    @Override
    public void displayMenu() throws IOException{
        out.println("== Management System Admin Menu ==");
        out.println("1. Add Book\n2. Delete Book\n3. See books\n4. See book\n5. Manage Users\n6. Exit");
        String choice = in.readLine();
        switch (choice) {
            case "1":
                addBookMenu();
                break;
            case "2":
                deleteBookMenu();
                break;
            case "3":
                seeBooksMenu();
                break;
            case "4":
                seeBookMenu();
                break;
            case "5":
                manageUsers();
                break;
            case "6":
                out.println("Goodbye !");
                out.println("/exit");
                break;
            default:
                System.out.println("Invalid choice");
                displayMenu();
                break;
        }

    }


    private void addBookMenu() throws IOException {
        // send command to clear screen
        try {
            out.println("title: ");
            String title = in.readLine();
            out.println("author: ");
            String author = in.readLine();
            out.println("description: ");
            String description = in.readLine();
            out.println("publisher: ");
            String publisher = in.readLine();
            out.println("published in: ");
            String published = in.readLine();
            Book newBook = new Book(title, author, description, publisher, published);
            boolean response = bookRepository.addBook(newBook);
            if (response) {
                out.println("Book added successfully");
                out.println("Press anything to go back to admin menu");
                in.readLine();
                displayMenu();
            }
        } catch (IllegalArgumentException e) {
            out.println("Date must be in this format: YYYY-MM-DD");
            out.println("Press anything to re enter book data");
            in.readLine();
            addBookMenu();
        }
    }

    private void deleteBookMenu() throws IOException {
        // clear screen
        out.println();
        out.println("Enter the ID of the book to be deleted: ");
        try {
            int id = Integer.parseInt(in.readLine());
            boolean response = bookRepository.deleteBook(id);
            if (response) {
                out.println("Book deleted successfully");
            } else {
                out.println("Book not found");
            }
            out.println("Press anything to go back to admin menu");
            in.readLine();
            displayMenu();
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID");
            deleteBookMenu();
        }
    }

    private void seeBooksMenu() throws IOException {
        out.println("== Library == ");
        List<Book> bookList = bookRepository.getAllBooks();
        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                out.println("Title: " + book.getTitle() + " id: " + book.getId());
            }
        } else {
            out.println("No books found");
        }
        out.println("Press anything to go back to admin menu");
        in.readLine();
        displayMenu();
    }

    private void seeBookMenu() throws IOException {
        out.println("1. Search by title\n2. Search by id\n3. Go back to admin menu");
        String choice = in.readLine();
        switch (choice) {
            case "1":
                out.println("Enter title: ");
                String title = in.readLine();
                Book retriviedBook = bookRepository.getBookByTitle(title);
                if (retriviedBook != null) {
                    displayBookInfo(retriviedBook);
                } else {
                    out.println("Book not found");
                }
                out.println("type something to go back to admin menu");
                in.readLine();
                displayMenu();
                break;
            case "2":
                out.println("Enter id: ");
                int id = Integer.parseInt(in.readLine());
                Book retrievedBook = bookRepository.getBookById(id);
                if (retrievedBook != null) {
                    displayBookInfo(retrievedBook);
                } else {
                    out.println("Book not found");
                }
                out.println("Press anything to go back to admin menu");
                in.readLine();
                displayMenu();
                break;
            default:
                seeBookMenu();
        }

    }

    private void manageUsers() throws IOException {
        out.println("== Users management menu ==");
        out.println("1. See users list\n2. See user info by ID\n3. See user info by username\n4. Delete user by ID\n5. Delete user by username\n6. Go back to admin menu");
        String choice = in.readLine();
        switch (choice) {
            case "1":
                List<User> userList = userRepository.getUsers();
                if (!userList.isEmpty()) {
                    for (User user : userList) {
                        out.println("ID: " + user.getUserID());
                        out.println("Username: " + user.getUsername());
                        out.println();
                    }
                        out.println("Press anything to go back to user management menu");
                        in.readLine();
                        manageUsers();
                } else {
                    out.println("No users found");
                    out.println();
                    out.println("Press anything to go back to user management menu");
                    in.readLine();
                    manageUsers();
                }
                break;
            case "2":
                out.println("Enter id: ");
                try {
                    int id = Integer.parseInt(in.readLine());
                    User myUser = userRepository.getUser(id);
                    if (myUser != null) {
                        displayUser(myUser);
                    } else {
                        out.println("User not found");
                    }
                    out.println("Press anything to go back to user management menu");
                    in.readLine();
                    manageUsers();
                } catch (NumberFormatException e) {
                    out.println("Invalid ID");
                    manageUsers();
                }
                break;
            case "3":
                out.println("Enter username: ");
                String username = in.readLine();
                User myUser = userRepository.getUser(username);
                if (myUser != null) {
                    displayUser(myUser);
                } else {
                    out.println("User not found");
                }

                out.println("Press anything to go back to user management menu");
                in.readLine();
                manageUsers();
                break;
            case "4":
                out.println("Enter id: ");
                try {
                    int id = Integer.parseInt(in.readLine());
                    if (id != 1) {
                        boolean isDelited = userRepository.deleteUser(id);
                        if (isDelited) {
                            out.println("User successfully deleted");
                        } else {
                            out.println("User not found");
                        }
                    } else {
                        out.println("You can't delete this user");
                    }
                    out.println("Press anything to go back to user management menu");
                    in.readLine();
                    manageUsers();
                } catch (NumberFormatException e) {
                    out.println("Invalid ID");
                    manageUsers();
                }
                break;
            case "5":
                out.println("Enter username: ");
                String username2 = in.readLine();
                if(!username2.equalsIgnoreCase("admin")) {
                    boolean isDelited = userRepository.deleteUser(username2);
                    if (isDelited) {
                        out.println("User successfully deleted");
                    } else {
                        out.println("User not found");
                    }
                }else{
                    out.println("You can't delete this user");
                }
                out.println("Press anything to go back to user management menu");
                in.readLine();
                manageUsers();
                break;
            case "6":
                displayMenu();
            default:
                manageUsers();
                break;
        }

    }

    public void displayBookInfo(Book book) {
        out.println("Id: " + book.getId());
        out.println("Title: " + book.getTitle());
        out.println("Author: " + book.getAuthor());
        out.println("Description: " + book.getDescription());
        out.println("publisher: " + book.getPublisher());
        out.println("published in: " + book.getPublishedDate());
    }

    public void displayUser(User user) {
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
}
