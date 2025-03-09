package com.bookstore.resource;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.impl.BookServiceImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private BookService bookService = new BookServiceImpl();

    @POST
    public Response createBook(Book book) {
        return bookService.createBook(book);
    }

    @GET
    public Response getAllBooks() {
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        return bookService.getBookById(id);
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        return bookService.updateBook(id, book);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        return bookService.deleteBook(id);
    }
}
