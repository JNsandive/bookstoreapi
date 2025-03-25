package com.bookstore.service.impl;

import com.bookstore.model.Author;
import com.bookstore.util.DataStorage;
import com.bookstore.service.AuthorService;
import com.bookstore.exception.AuthorNotFoundException;
import com.bookstore.exception.InvalidInputException;
import com.bookstore.model.Book;
import com.bookstore.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.Response;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);
    private DataStorage dataStorage = DataStorage.getInstance();

    // Create Author Method
    @Override
    public Response createAuthor(Author author) {
        try {
            if (author == null) {
                throw new InvalidInputException("Invalid author data.");
            }

            for (Author existingAuthor : dataStorage.getAuthors()) {
                if (existingAuthor.getEmail().equalsIgnoreCase(author.getEmail())) {
                    throw new InvalidInputException("Email already exists.");
                }
            }

            Author createdAuthor = dataStorage.addAuthor(author);
            return Response.status(Response.Status.CREATED).entity(createdAuthor).build();
        } catch (InvalidInputException e) {
            logger.error("Invalid author data: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid author data.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("An error occurred while creating the author: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while creating the author.", e.getMessage()))
                    .build();
        }
    }

    // Extract All Authors Details
    @Override
    public Response getAllAuthors() {
        try {
            List<Author> authors = dataStorage.getAuthors();
            if (authors.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity(new ErrorResponse("No authors available.", "The database returned an empty list."))
                        .build();
            }
            return Response.ok(authors).build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching authors: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while fetching authors.", e.getMessage()))
                    .build();
        }
    }

    // Extract Particular Author
    @Override
    public Response getAuthorById(String id) {
        try {
            Author author = dataStorage.getAuthorById(id);
            if (author == null) {
                throw new AuthorNotFoundException("Author with ID " + id + " not found.");
            }
            return Response.ok(author).build();
        } catch (AuthorNotFoundException e) {
            logger.error("Author not found: {}", e.getMessage(), e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Author not found.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching the author: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while fetching the author.", e.getMessage()))
                    .build();
        }
    }

    // Update Author
    @Override
    public Response updateAuthor(String id, Author author) {
        try {
            if (author == null) {
                throw new InvalidInputException("Invalid author data.");
            }
            
            author.setAuthorId(id);
            for (Author a : dataStorage.getAuthors()) {
                if (!a.getAuthorId().equals(id)) {
                    if (a.getEmail().equalsIgnoreCase(author.getEmail())) {
                        throw new InvalidInputException("Email already exists.");
                    }
                }
            }
            Author updatedAuthor = dataStorage.updateAuthor(id, author);
            if (updatedAuthor == null) {
                throw new AuthorNotFoundException("Author with ID " + id + " not found.");
            }

            return Response.ok(updatedAuthor).build();
        } catch (AuthorNotFoundException e) {
            logger.error("Author not found for update: {}", e.getMessage(), e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Author not found for update.", e.getMessage()))
                    .build();
        } catch (InvalidInputException e) {
            logger.error("Invalid author data for update: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid author data.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("An error occurred while updating the author: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while updating the author.", e.getMessage()))
                    .build();
        }
    }

    // Delete Author 
    @Override
    public Response deleteAuthor(String id) {
        try {
            boolean success = dataStorage.removeAuthor(id);
            if (!success) {
                throw new AuthorNotFoundException("Author with ID " + id + " not found for deletion.");
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (AuthorNotFoundException e) {
            logger.error("Author not found for deletion: {}", e.getMessage(), e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Author not found for deletion.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("An error occurred while deleting the author: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while deleting the author.", e.getMessage()))
                    .build();
        }
    }

    // Get Books Related To The Author
    @Override
    public Response getBooksByAuthor(String id) {
        try {
            List<Book> books = dataStorage.getBooksByAuthor(id);
            if (books.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity(new ErrorResponse("No books found for the author.", "The author exists but has no books."))
                        .build();
            }
            return Response.ok(books).build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching books by author: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("An error occurred while fetching books by author.", e.getMessage()))
                    .build();
        }
    }

}
