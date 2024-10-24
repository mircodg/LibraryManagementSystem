package com.library.management.books;

import java.sql.Date;

public class Book {
    private int id = 0;
    private String title;
    private String author;
    private String description;
    private String publisher;
    private String publishedDate;
    private Integer userID;

    public Book(int id, String title, String author, String description, String publisher, String publishedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
    }

    public Book(String title, String author, String description, String publisher, String publishedDate) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        if (this.author != null)
            return author;
        return null;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDescription() {
        if (this.description != null)
            return description;
        return null;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPublisher() {
        if (this.publisher != null)
            return publisher;
        return null;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getPublishedDate() {
        if (this.publishedDate != null)
            return publishedDate;
        return null;
    }
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
    public Date stringToDate(String publishedDate) throws IllegalArgumentException{
        return Date.valueOf(publishedDate);
    }
    public int getId() {
        return id;
    }
    public void setUserID(int id) {
        this.userID = id;
    }
    public int getUserID() {
        return userID;
    }
}
