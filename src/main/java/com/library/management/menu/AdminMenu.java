package com.library.management.menu;

import com.library.management.books.Book;
import com.library.management.database.BookRepository;
import com.library.management.users.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class AdminMenu extends ClientMenu {
    BookRepository bookRepository;

    public AdminMenu(User user, Socket socket, Connection connection) {
        super(socket, connection);
        bookRepository = new BookRepository(connection);
    }

    @Override
    public void displayMenu() {
        out.println("== Management System Admin Menu ==");
        out.println("1. Add Book\n2. Delete Book\n3. See books\n4. See book\n5. See Users\n6. Delete User\n7. Exit");
        try {
            int choice = Integer.parseInt(in.readLine());
            switch (choice) {
                case 1:
                    addBookMenu();
                    break;
                case 2:
                    System.out.println("Delete Book");
                    displayMenu();
                    break;
//                case 3:
//                    System.out.println("Edit Book");
//                    displayMenu();
//                    break;
                case 3:
                    seeBooksMenu();
                    break;
                case 4:
                    System.out.println("Delete User");
                    seeBookMenu();
                    break;
                case 5:
                    System.out.println("Inspect User");
                    break;
                case 6:
                    System.out.println("exiting...");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } catch (IOException e) {
            System.err.println("Error while reading from input. " + e.getMessage());
        }
    }


    private void addBookMenu() {
        // send command to clear screen
        try {
            out.println("Add Book");
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
                while (true) {
                    int choice = Integer.parseInt(in.readLine());
                    out.println("Press 1 to go back to admin menu");
                    if (choice == 1) {
                        displayMenu();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while reading from input in addBookMenu. " + e.getMessage());
        }
    }

    private void deleteBookMenu() {
    }

    private void seeBooksMenu() {
        out.println("== Library == ");
        List<Book> bookList = bookRepository.getAllBooks();
        for (Book book : bookList) {
            out.println("Title: " + book.getTitle() + " id: " + book.getId());
        }
        out.println("Press 1 to go back to admin menu");
        while (true) {
            try {
                int choice = Integer.parseInt(in.readLine());
                if (choice == 1) {
                    displayMenu();
                }
            } catch (IOException e) {
                System.err.println("Error while reading from input. " + e.getMessage());
                break;
            }

        }
    }

    private void seeBookMenu() {
        out.println("1. Search by title\n2. Search by id\n3. Go back to admin menu");
        while (true) {
            try {
                int choice = Integer.parseInt(in.readLine());
                switch (choice){
                    case 1:
                        out.println("Enter title: ");
                        String title = in.readLine();
                        Book retriviedBook = bookRepository.getBookByTitle(title);
                        if(retriviedBook != null){
                            displayBookInfo(retriviedBook);
                        }else{
                            out.println("Book not found");
                        }
                        out.println("type something to go back to admin menu");
                        in.readLine();
                        displayMenu();
                        break;
                    case 2:
                        out.println("Enter id: ");
                        int id = Integer.parseInt(in.readLine());
                        Book retrievedBook = bookRepository.getBookById(id);
                        if(retrievedBook != null){
                            displayBookInfo(retrievedBook);
                        }else {
                            out.println("Book not found");
                        }
                        out.println("Press 1 to go back to admin menu");
                        int sndChoice2 = Integer.parseInt(in.readLine());
                        if (sndChoice2 == 1) {
                            displayMenu();
                        }
                        break;
                    default:
                        displayMenu();
                }
            } catch (IOException e) {
                System.err.println("Error while reading from input. " + e.getMessage());
            }

        }
    }

    private void editBookMenu() {
    }

    private void seeUsersMenu() {
    }

    private boolean deleteUser() {
        return false;
    }

    public void displayBookInfo(Book book) {
        out.println("Id: " + book.getId());
        out.println("Title: " + book.getTitle());
        out.println("Author: " + book.getAuthor());
        out.println("Description: " + book.getDescription());
        out.println("publisher: " + book.getPublisher());
        out.println("published in: " + book.getPublishedDate());
    }
}
