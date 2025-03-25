package com.bookstore.dao;

import javax.validation.constraints.Min;

public class CartItemRequest {
    private int bookId;

    @Min(value = 1, message = "Count must be greater than zero.")
    private int count;

    // Getters and setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
