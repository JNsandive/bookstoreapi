package com.bookstore.model;

public class CartItem {
    private int bookId;
    private String bookName;
    private int count;

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
