package com.bookstore.model;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderItem {
    @NotNull(message = "Book cannot be null.")
    @Valid  
    private Book book;

    @NotNull(message = "Count is required.")
    @Positive(message = "Count must be greater than 0.")
    @Min(value = 1, message = "Count must be at least 1.")
    private Integer count;

    public OrderItem(Book book, int count) {
        this.book = book;
        this.count = count;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
