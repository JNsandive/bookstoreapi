package com.bookstore.service;

import com.bookstore.model.Book;
import javax.ws.rs.core.Response;

public interface BookService {
    
    Response createBook(Book book);
    Response getAllBooks();
    Response getBookById(int id);
    Response updateBook(int id, Book book);
    Response deleteBook(int id);
}
