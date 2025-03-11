package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String customerId;
    private List<CartItem> cartItems;  // List of CartItems

    public Cart(String customerId) {
        this.customerId = customerId;
        this.cartItems = new ArrayList<>();
    }

    // Getter and Setter
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Add a book to the cart
    public boolean addBook(Book book, int count) {
        if (count <= 0) {
            return false;  // Invalid count
        }

        // Check if the book already exists in the cart
        for (CartItem cartItem : cartItems) {
            if (cartItem.getBookId() == book.getId()) {
                cartItem.setCount(cartItem.getCount() + count);
                return true;
            }
        }

        // If not, add a new CartItem to the cart
        cartItems.add(new CartItem(book.getId(), book.getTitle(), count));
        return true;
    }

    // Remove a book from the cart
    public boolean removeBook(int bookId) {
        return cartItems.removeIf(cartItem -> cartItem.getBookId() == bookId);
    }

    // Update the count of a book in the cart
    public boolean updateBookCount(int bookId, int newCount) {
        if (newCount <= 0) {
            return false;  // Invalid count
        }

        for (CartItem cartItem : cartItems) {
            if (cartItem.getBookId() == bookId) {
                cartItem.setCount(newCount);
                return true;
            }
        }
        return false;  // Book not found
    }

    // Get the count of a book in the cart
    public int getBookCount(int bookId) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getBookId() == bookId) {
                return cartItem.getCount();
            }
        }
        return 0;  // Book not found
    }
}
