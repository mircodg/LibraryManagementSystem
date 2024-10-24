package com.library.management.database;

import com.library.management.books.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;


public class BookRepository {

    private final Connection connection;

    public BookRepository(Connection connection) {
        this.connection = connection;
    }


    public boolean addBook(Book book) throws IllegalArgumentException{
        try {
            String query = "INSERT INTO books (title, description, author, publisher, publishedDate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getDescription());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setDate(5, book.stringToDate(book.getPublishedDate())); // handle date correctly, see chatgpt
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("error while adding a book: " + e.getMessage());
            return false;
        }
    }

    public List<Book> getAllBooks() {
        try {
            String query = "SELECT * FROM books";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                Date date = rs.getDate("publishedDate");
                String dateAsString = date.toString();
                books.add(new Book(rs.getInt("bookID"), rs.getString("title"), rs.getString("description"), rs.getString("author"), rs.getString("publisher"), dateAsString, rs.getInt("userID")));
            }
            return books;
        } catch (SQLException e) {
            System.err.println("error while retrieving all books: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteBook(int bookID) {
        try {
            String query = "DELETE FROM books WHERE bookID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("error while deleting book: " + e.getMessage());
            return false;
        }
    }

    public Book getBookByTitle(String title) {
        try {
            String query = "SELECT * FROM books WHERE title like ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            return getBookFromQuery(rs);
        } catch (SQLException e) {
            System.err.println("error while searching a book: " + e.getMessage());
            return null;
        }
    }

    public Book getBookById(int bookID) {
        try{
            String query = "SELECT * FROM books WHERE bookID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookID);
            ResultSet rs = statement.executeQuery();
            return getBookFromQuery(rs);
        }catch(SQLException e){
            System.err.println("error while searching a book: " + e.getMessage());
            return null;
        }
    }

    public List<Book> getUserBooks(int userID){
        try{
            String query = "SELECT * FROM books WHERE userID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            List<Book> books = new ArrayList<>();
            return getBooksFromQuery(rs, books);
        }catch(SQLException e){
            System.err.println("error while getting user books: " + e.getMessage());
            return null;
        }
    }

    private Book getBookFromQuery(ResultSet rs) throws SQLException {
        if(rs.next()) {
            Date date = rs.getDate("publishedDate");
            String dateAsString = date.toString();
            return new Book(rs.getInt("bookID"), rs.getString("title"), rs.getString("author"), rs.getString("description"), rs.getString("publisher"), dateAsString, rs.getInt("userID"));
        }return null;
    }

    private List<Book> getBooksFromQuery(ResultSet rs, List<Book> books) throws SQLException {
        while (rs.next()) {
            Date date = rs.getDate("publishedDate");
            String dateAsString = date.toString();
            books.add(new Book(rs.getInt("bookID"), rs.getString("title"), rs.getString("author"), rs.getString("description"), rs.getString("publisher"), dateAsString, rs.getInt("userID") ));
        }
        return books;
    }

    public boolean returnBook(int userID, int bookID){
        try{
            String query = "UPDATE books SET userID = NULL WHERE bookID = ? AND userID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookID);
            statement.setInt(2, userID);
            return statement.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("error while returning book: " + e.getMessage());
            return false;
        }
    }

    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        try {
            String query = "SELECT * FROM books WHERE userID IS NULL";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            return getBooksFromQuery(rs, books);
        } catch (SQLException e) {
            System.err.println("error while getting available books: " + e.getMessage());
            return null;
        }
    }

    public boolean rentBook(int bookID, int userID){
        try{
            Book book = getBookById(bookID);
            // if the book exist, and it's not rented by any user
            if(book != null  && book.getUserID() == 0){
                System.out.println("trying to rent book");
                String query = "UPDATE books SET userID = ? WHERE bookID = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userID);
                statement.setInt(2, bookID);
                return statement.executeUpdate() > 0;
            }else{
                return false;
            }
        }catch(SQLException e){
            System.err.println("error while renting book: " + e.getMessage());
            return false;
        }
    }
}
