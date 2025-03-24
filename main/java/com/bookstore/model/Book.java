package com.bookstore.model;

import com.bookstore.util.DataStorage;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import javax.validation.constraints.*;

public class Book {

    private int id;
    @NotNull(message = "Title cannot be null.")
    private String title;

    private Author author;

    @NotNull(message = "ISBN cannot be null.")
    private String isbn;

    @NotNull(message = "Stock is required and cannot be null.")
    @Positive(message = "Stock must be greater than 0.")
    @Min(value = 1, message = "Stock must be greater than 0.")
    private Integer stock;  
    
    @NotNull(message = "Price is required and cannot be null.")
    @Positive(message = "Price must be greater than 0.")
    private Double price;  

    public Book() {
    }

    public Book(int id, String title, Author author, String isbn, int stock, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.stock = stock;
        this.price = price;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @JsonSetter("authorId")  // Make sure this is here to map "authorId" from JSON to setter
    public void setAuthorById(String authorId) {
        Author author = DataStorage.getInstance().getAuthorById(authorId);
        if (author != null) {
            this.author = author;
        }
    }

    @JsonGetter("authorId")
    public String getAuthorId() {
        if (author != null) {
            return author.getAuthorId();
        }
        return null;
    }
}
