package com.bookstore.service.impl;

import com.bookstore.model.Book;
import com.bookstore.util.DataStorage;
import com.bookstore.service.BookService;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.List;

public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private DataStorage dataStorage = DataStorage.getInstance();

    // Create book method
    @Override
    public Response createBook(Book book) {
        try {
            if (book == null) {
                throw new IllegalArgumentException("Invalid book data.");
            }

            // Check if the author exists
            if (book.getAuthor() == null) {
                logger.error("Author with ID {} does not exist.", book.getAuthorId());
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Author with ID " + book.getAuthorId() + " does not exist.")
                        .build();
            }

            // Call addBook which will check for author validity
            Book createdBook = dataStorage.addBook(book);
            logger.info("Successfully created book: {}", createdBook);

            return Response.status(Response.Status.CREATED).entity(createdBook).build();  // Return newly created book

        } catch (IllegalArgumentException e) {
            logger.error("Error creating book: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid request data.") // Standard message
                    .build();
        } catch (InvalidInputException e) {
            logger.error("Error creating book: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid request data.") // Standard message
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error while creating book: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.") // Standard message
                    .build();
        }
    }

    // Update book method
    @Override
    public Response updateBook(int id, Book book) {
        try {
            if (book == null) {
                throw new InvalidInputException("Invalid book data.");
            }

            // Check if the author exists for the updated book
            if (book.getAuthor() == null) {
                logger.error("Author with ID {} does not exist for update.", book.getAuthorId());
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Author with ID " + book.getAuthorId() + " does not exist.")
                        .build();
            }

            // Update the book in storage
            Book updatedBook = dataStorage.updateBook(id, book);

            if (updatedBook == null) {
                throw new BookNotFoundException("Book with ID " + id + " not found.");
            }

            logger.info("Successfully updated book: {}", updatedBook);

            return Response.ok(updatedBook).build();
        } catch (BookNotFoundException e) {
            logger.error("Book not found for update: {}", e.getMessage(), e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found for update.")
                    .build();
        } catch (InvalidInputException e) {
            logger.error("Invalid book data for update: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid book data.")
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error while updating book: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while updating the book.")
                    .build();
        }
    }

    // Extract All Books
    @Override
    public Response getAllBooks() {
        try {
            List<Book> books = dataStorage.getBooks();
            if (books.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("No books available.")
                        .build();
            }
            return Response.ok(books).build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching books: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching books.")
                    .build();
        }
    }

    // Extract Specific Book
    @Override
    public Response getBookById(int id) {
        try {
            Book book = dataStorage.getBookById(id);
            if (book == null) {
                throw new BookNotFoundException("Book with ID " + id + " not found.");
            }
            return Response.ok(book).build();
        } catch (BookNotFoundException e) {
            logger.error("Book not found: {}", e.getMessage(), e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found.")
                    .build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching the book: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching the book.")
                    .build();
        }
    }

    // Delete book
    @Override
    public Response deleteBook(int id) {
        try {
            boolean success = dataStorage.removeBook(id);
            if (!success) {
                throw new BookNotFoundException("Book with ID " + id + " not found for deletion.");
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (BookNotFoundException e) {
            logger.error("Book not found for deletion: {}", e.getMessage(), e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found for deletion.")
                    .build();
        } catch (Exception e) {
            logger.error("An error occurred while deleting the book: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while deleting the book.")
                    .build();
        }
    }
}
