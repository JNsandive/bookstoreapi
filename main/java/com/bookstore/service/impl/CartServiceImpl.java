package com.bookstore.service.impl;

import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.util.DataStorage;
import com.bookstore.service.CartService;
import com.bookstore.dao.CartItemRequest;
import com.bookstore.model.ErrorResponse;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private DataStorage dataStorage = DataStorage.getInstance();

    // Add Cart Item Method
    @Override
    public Response addItemToCart(String customerId, CartItemRequest cartItemRequest) {
        try {
            // Fetch the book from the DataStorage
            Book book = dataStorage.getBookById(cartItemRequest.getBookId());
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Book not found.", "No book found with the given ID."))
                        .build();
            }

            // Check if the requested count does not exceed the available stock
            if (cartItemRequest.getCount() > book.getStock()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Stock limit exceeded.", "Requested count exceeds available stock."))
                        .build();
            }

            // Fetch the customer's cart, if it doesn't exist, create a new one
            Cart cart = dataStorage.getCustomerCart(customerId);
            if (cart == null) {
                // No cart exists for the customer, create a new one
                cart = new Cart(customerId);
                dataStorage.addCart(cart);  // Assuming addCart method is defined in DataStorage
            }

            // Add the book to the cart
            cart.addBook(book, cartItemRequest.getCount());
            dataStorage.updateCart(cart);

            return Response.status(Response.Status.CREATED)
                    .entity("Book added to cart successfully.")
                    .build();
        } catch (Exception e) {
            logger.error("Error adding item to cart", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while adding the item to the cart."))
                    .build();
        }
    }

    // Get Cart Item Method
    @Override
    public Response getCartItems(String customerId) {
        try {
            Cart cart = dataStorage.getCustomerCart(customerId);
            if (cart == null) {
                logger.error("Cart for customer {} not found", customerId);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Customer cart not found.", "No cart found for the given customer ID."))
                        .build();
            }

            return Response.ok(cart.getCartItems()).build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching cart items for customer {}", customerId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while fetching cart items."))
                    .build();
        }
    }

    // Update Cart Item Method
    @Override
    public Response updateCartItem(String customerId, int bookId, CartItemRequest cartItemRequest) {
        try {
            Book book = dataStorage.getBookById(bookId);
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Book not found.", "No book found with the given ID."))
                        .build();
            }

            if (cartItemRequest.getCount() > book.getStock()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Stock limit exceeded.", "Requested count exceeds available stock."))
                        .build();
            }

            // Fetch the customer's cart
            Cart cart = dataStorage.getCustomerCart(customerId);
            if (cart == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Customer cart not found.", "No cart found for the given customer ID."))
                        .build();
            }

            // Update the book count in the cart
            boolean updated = cart.updateBookCount(bookId, cartItemRequest.getCount());
            if (updated) {
                dataStorage.updateCart(cart);
                return Response.ok("Book updated in cart successfully.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Book not found in cart.", "The book is not present in the cart or invalid count."))
                        .build();
            }
        } catch (Exception e) {
            logger.error("An error occurred while updating the cart item for customer {}", customerId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while updating the cart item."))
                    .build();
        }
    }

    // Remove Item From The Cart Method
    @Override
    public Response removeItemFromCart(String customerId, int bookId) {
        try {
            Cart cart = dataStorage.getCustomerCart(customerId);
            if (cart == null) {
                logger.error("Cart for customer {} not found", customerId);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Customer cart not found.", "No cart found for the given customer ID."))
                        .build();
            }

            boolean removed = cart.removeBook(bookId);
            if (removed) {
                dataStorage.updateCart(cart);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                logger.error("Book with ID {} not found in cart for customer {}", bookId, customerId);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Book not found in cart.", "No book found with the given ID in the cart."))
                        .build();
            }
        } catch (Exception e) {
            logger.error("An error occurred while removing the item from the cart for customer {}", customerId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while removing the item from the cart."))
                    .build();
        }
    }

}
