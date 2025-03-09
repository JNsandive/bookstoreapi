package com.bookstore.util;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class DataStorage {

    private static DataStorage instance;

    // In-memory storage for Books and Authors
    private Map<Integer, Book> books = new HashMap<>();
    private Map<String, Author> authors = new HashMap<>();
    private Map<String, Customer> customers = new HashMap<>();

    private static int bookIdCounter = 1;  // To track the IDs for books

    private DataStorage() {
        // Initialize with some default data if required
        Author author1 = new Author("J.K. Rowling", "rowling@example.com", "password123", "British author");
        authors.put(author1.getAuthorId(), author1);

        // Create and add a mock customer
        Customer customer1 = new Customer("Customer Tishan", "tishan@example.com", "password123");
        customers.put(customer1.getCustomerId(), customer1);  

        // Create a mock book and add it
        Book book1 = new Book(bookIdCounter++, "Harry Potter", author1, "978-3-16-148410-0", 10, 29.99);
        books.put(book1.getId(), book1);
    }

    // Singleton pattern to get the instance
    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    // Add Author to the storage
    public Author addAuthor(Author author) {
        if (authors.containsKey(author.getAuthorId())) {
            return authors.get(author.getAuthorId());  // Return the existing author if already present
        }
        authors.put(author.getAuthorId(), author);
        return author;
    }

    public Book addBook(Book book) {
        // If the author exists, proceed with adding the book
        book.setId(bookIdCounter++);
        books.put(book.getId(), book);
        return book;
    }

    // Get all Authors
    public List<Author> getAuthors() {
        return authors.values().stream().collect(Collectors.toList());
    }

    // Get an Author by ID
    public Author getAuthorById(String authorId) {
        return authors.get(authorId);
    }

    // Update an Author by ID
    public Author updateAuthor(String authorId, Author author) {
        if (authors.containsKey(authorId)) {
            authors.put(authorId, author);
            return author;
        }
        return null;
    }

    // Remove an Author by ID
    public boolean removeAuthor(String authorId) {
        return authors.remove(authorId) != null;
    }

    // Get all Books
    public List<Book> getBooks() {
        return books.values().stream().collect(Collectors.toList());
    }

    // Get a Book by ID
    public Book getBookById(int bookId) {
        return books.get(bookId);
    }

    // Update a Book by ID
    public Book updateBook(int bookId, Book book) {
        if (books.containsKey(bookId)) {
            book.setId(bookId);
            books.put(bookId, book);
            return book;
        }
        return null;  // Book not found
    }

    // Remove a Book by ID
    public boolean removeBook(int bookId) {
        return books.remove(bookId) != null;
    }

    // Get Books by Author ID
    public List<Book> getBooksByAuthor(String authorId) {
        return books.values().stream()
                .filter(book -> book.getAuthor() != null && book.getAuthor().getAuthorId().equals(authorId))
                .collect(Collectors.toList());
    }

    // Add Customer to storage
    public Customer addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);  // Using the auto-generated customerId
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
}
