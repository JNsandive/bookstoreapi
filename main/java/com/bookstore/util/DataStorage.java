package com.bookstore.util;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.model.Customer;
import com.bookstore.model.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class DataStorage {

    private static DataStorage instance;

    // In-memory storages
    private Map<Integer, Book> books = new HashMap<>();
    private Map<String, Author> authors = new HashMap<>();
    private Map<String, Customer> customers = new HashMap<>();
    private Map<String, Cart> carts = new HashMap<>();
    private Map<String, List<Order>> orders = new HashMap<>();

    private static int bookIdCounter = 1;

    // Mock data inisializing constructor
    private DataStorage() {

        Author author1 = new Author("J.K. Rowling", "rowling@example.com", "password123", "British author");
        authors.put(author1.getAuthorId(), author1);

        Customer customer1 = new Customer("Customer Tishan", "tishan@example.com", "password123");
        customers.put(customer1.getCustomerId(), customer1);

        Book book1 = new Book(bookIdCounter++, "Harry Potter", author1, "978-3-16-148410-0", 10, 29.99);
        books.put(book1.getId(), book1);

        Cart cart1 = new Cart(customer1.getCustomerId());
        cart1.addBook(book1, 2);
        carts.put(customer1.getCustomerId(), cart1);

    }

    // Singleton instance
    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    // Add Author
    public Author addAuthor(Author author) {
        if (authors.containsKey(author.getAuthorId())) {
            return authors.get(author.getAuthorId());
        }
        authors.put(author.getAuthorId(), author);
        return author;
    }

    // Add Book
    public Book addBook(Book book) {
        book.setId(bookIdCounter++);
        books.put(book.getId(), book);
        return book;
    }

    // Get all Authors
    public List<Author> getAuthors() {
        return authors.values().stream().collect(Collectors.toList());
    }

    // Get Author by ID
    public Author getAuthorById(String authorId) {
        return authors.get(authorId);
    }

    // Update Author
    public Author updateAuthor(String authorId, Author author) {
        if (authors.containsKey(authorId)) {
            authors.put(authorId, author);
            return author;
        }
        return null;
    }

    // Remove Author
    public boolean removeAuthor(String authorId) {
        return authors.remove(authorId) != null;
    }

    // Get all Books
    public List<Book> getBooks() {
        return books.values().stream().collect(Collectors.toList());
    }

    // Get Book by ID
    public Book getBookById(int bookId) {
        return books.get(bookId);
    }

    // Update a Book
    public Book updateBook(int bookId, Book book) {
        if (books.containsKey(bookId)) {
            book.setId(bookId);
            books.put(bookId, book);
            return book;
        }
        return null;
    }

    // Remove a Book
    public boolean removeBook(int bookId) {
        return books.remove(bookId) != null;
    }

    // Get Books by Author
    public List<Book> getBooksByAuthor(String authorId) {
        return books.values().stream()
                .filter(book -> book.getAuthor() != null
                && book.getAuthor().getAuthorId().equals(authorId))
                .collect(Collectors.toList());
    }

    // Add Customer
    public Customer addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        return customer;
    }

    // Get all Customers
    public List<Customer> getCustomers() {
        return customers.values().stream().collect(Collectors.toList());
    }

    // Get Customer by ID
    public Customer getCustomerById(String customerId) {
        return customers.get(customerId);
    }

    // Update Customer
    public Customer updateCustomer(String customerId, Customer customer) {
        if (customers.containsKey(customerId)) {
            customers.put(customerId, customer);
            return customer;
        }
        return null;
    }

    // Remove Customer
    public boolean removeCustomer(String customerId) {
        return customers.remove(customerId) != null;
    }

    // Get Customer's Cart
    public Cart getCustomerCart(String customerId) {
        return carts.get(customerId);
    }

    // In DataStorage class
    public void addCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }

    // Update Cart
    public void updateCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }

    // Save Order
    public void saveOrder(Order order) {
        orders.putIfAbsent(order.getCustomerId(), new ArrayList<>());
        orders.get(order.getCustomerId()).add(order);
    }

    // Get Orders by Customer ID
    public List<Order> getOrdersByCustomerId(String customerId) {
        return orders.getOrDefault(customerId, new ArrayList<>());
    }

    // Get Order by Customer ID and Order ID
    public Order getOrderById(String customerId, String orderId) {
        return orders.getOrDefault(customerId, new ArrayList<>())
                .stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }
}
