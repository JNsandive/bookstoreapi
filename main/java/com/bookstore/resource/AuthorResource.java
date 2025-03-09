package com.bookstore.resource;

import com.bookstore.model.Author;
import com.bookstore.service.AuthorService;
import com.bookstore.service.impl.AuthorServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private AuthorService authorService = new AuthorServiceImpl();

    @POST
    public Response createAuthor(Author author) {
        return authorService.createAuthor(author);
    }

    @GET
    public Response getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") String id) {
        return authorService.getAuthorById(id);
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") String id, Author author) {
        return authorService.updateAuthor(id, author);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") String id) {
        return authorService.deleteAuthor(id);
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") String id) {
        return authorService.getBooksByAuthor(id);
    }
}
