package com.bookstore.service;

import com.bookstore.model.Author;
import javax.ws.rs.core.Response;

public interface AuthorService {
    Response createAuthor(Author author);
    Response getAllAuthors();
    Response getAuthorById(String id);
    Response updateAuthor(String id, Author author);
    Response deleteAuthor(String id);
    Response getBooksByAuthor(String id);
}
