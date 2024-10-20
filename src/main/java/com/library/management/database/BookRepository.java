package com.library.management.database;
import com.library.management.books.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class BookRepository {

    private final Connection connection;

    public BookRepository(Connection connection) {
        this.connection = connection;
    }

    public Book getBookByID(int bookID) {
        try{
            String query = "SELECT * FROM books WHERE bookID = ?" ;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookID);
            ResultSet rs = statement.executeQuery();
            return new Book(rs.getInt("bookID"), rs.getString("title"), rs.getString("author"), rs.getString("description"), rs.getString("publisher"), rs.getDate("publishedDate"));
        }catch(SQLException e){
            System.err.println("error while retrieving book: " + e.getMessage());
            return null;
        }
    }

    public boolean addBook(Book book) {
        try{
            String query = "INSERT INTO books (bookID, title, description, author, publisher, publishedDate) VALUES (?, ?, ?, ?, ?, ?))";
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.execute();
        }catch(SQLException e){
            System.err.println("error while adding a book: " + e.getMessage());
            return false;
        }
    }

    public List<Book> getAllBooks() {
        try{
            String query = "SELECT * FROM books";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            List<Book> books = new ArrayList<>();
            while(rs.next()){
                books.add(new Book(rs.getInt("bookID"), rs.getString("title"), rs.getString("description"), rs.getString("author"), rs.getString("publisher"), rs.getDate("publishedDate") ));
            }
            return books;
        }catch(SQLException e){
            System.err.println("error while retrieving all books: " + e.getMessage());
            return null;
        }
    }

    public boolean updateBook(int bookID, String title, String author, String description, String publisher, Date date) {
        Book newBook = getBookByID(bookID);
        if(newBook == null)
            return false;
        if(title != null)
            newBook.setTitle(title);
        if(author != null)
            newBook.setAuthor(author);
        if(description != null)
            newBook.setDescription(description);
        if(publisher != null)
            newBook.setPublisher(publisher);
        if(date != null)
            newBook.setPublishedDate(date);
        return addBook(newBook);
    }


    public boolean deleteBook(int bookID) {
        try{
            String query = "DELETE FROM books WHERE bookID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookID);
            return statement.execute();
        }catch(SQLException e){
            System.err.println("error while deleting book: " + e.getMessage());
            return false;
        }
    }

}
