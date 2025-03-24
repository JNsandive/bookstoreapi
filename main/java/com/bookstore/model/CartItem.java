package com.bookstore.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItem {

    @NotNull(message = "Book ID is required.")
    @Positive(message = "Book ID must be greater than 0.")
    private int bookId;

    private String bookName;

    @NotNull(message = "Count is required.")
    @Min(value = 1, message = "Count must be at least 1.")
    private Integer count;

    public CartItem(int bookId, String bookName, int count) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.count = count;
    }

    // Getter and Setter
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
