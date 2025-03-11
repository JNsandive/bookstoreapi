package com.bookstore.resource;

import com.bookstore.dao.CartItemRequest;
import com.bookstore.service.CartService;
import com.bookstore.service.impl.CartServiceImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers/{customerId}/cart")
public class CartResource {

    private CartService cartService = new CartServiceImpl();

    @POST
    @Path("/items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItemToCart(@PathParam("customerId") String customerId, CartItemRequest cartItemRequest) {
        return cartService.addItemToCart(customerId, cartItemRequest);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartItems(@PathParam("customerId") String customerId) {
        return cartService.getCartItems(customerId);
    }

    @PUT
    @Path("/items/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartItem(@PathParam("customerId") String customerId, @PathParam("bookId") int bookId, CartItemRequest cartItemRequest) {
        return cartService.updateCartItem(customerId, bookId, cartItemRequest);
    }

    @DELETE
    @Path("/items/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeItemFromCart(@PathParam("customerId") String customerId, @PathParam("bookId") int bookId) {
        return cartService.removeItemFromCart(customerId, bookId);
    }
}
