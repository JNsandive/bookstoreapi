package com.bookstore.service.impl;

import com.bookstore.model.Book;
import com.bookstore.util.DataStorage;
import com.bookstore.service.BookService;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.InvalidInputException;
import com.bookstore.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.List;
import javax.validation.Valid;

public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private DataStorage dataStorage = DataStorage.getInstance();

    // Create book method
    @Override
    public Response createBook(@Valid Book book) {
        try {
            if (book == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Invalid book data.", "The provided book data is null."))
                        .build();
            }

            // Extract authorId from book and check if author exists
            String authorId = book.getAuthorId();
            if (authorId == null || DataStorage.getInstance().getAuthorById(authorId) == null) {
                logger.error("Author ID does not exist.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Invalid author ID.", "The specified author ID is invalid."))
                        .build();
            }

            // Proceed with adding the book
            Book createdBook = dataStorage.addBook(book);
            logger.info("Successfully created book: {}", createdBook);
            return Response.status(Response.Status.CREATED).entity(createdBook).build();

        } catch (IllegalArgumentException e) {
            logger.error("Error creating book: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid request data.", "The book data provided is not valid."))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error while creating book: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An unexpected error occurred while processing the request."))
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
                        .entity(new ErrorResponse("Invalid author ID.", "Author with ID " + book.getAuthorId() + " does not exist."))
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
                    .entity(new ErrorResponse("Book not found for update.", e.getMessage()))
                    .build();
        } catch (InvalidInputException e) {
            logger.error("Invalid book data for update: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid book data.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error while updating book: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while updating the book.", e.getMessage()))
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
                        .entity(new ErrorResponse("No books available.", "The database returned an empty book list."))
                        .build();
            }
            return Response.ok(books).build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching books: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while fetching books.", e.getMessage()))
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
                    .entity(new ErrorResponse("Book not found.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching the book: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while fetching the book.", e.getMessage()))
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
                    .entity(new ErrorResponse("Book not found for deletion.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("An error occurred while deleting the book: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while deleting the book.", e.getMessage()))
                    .build();
        }
    }

}
