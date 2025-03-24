package com.bookstore.service.impl;

import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.util.DataStorage;
import com.bookstore.service.OrderService;
import com.bookstore.exception.CustomerNotFoundException;
import com.bookstore.exception.OutOfStockException;
import com.bookstore.exception.CartNotFoundException;
import com.bookstore.model.CartItem;
import com.bookstore.model.Customer;
import com.bookstore.model.ErrorResponse;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderServiceImpl implements OrderService {

    private DataStorage dataStorage = DataStorage.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Response placeOrder(String customerId) {
        try {
            // Validate customer
            Customer customer = dataStorage.getCustomerById(customerId);
            if (customer == null) {
                throw new CustomerNotFoundException("Customer not found.");
            }

            // Validate cart
            Cart cart = dataStorage.getCustomerCart(customerId);
            if (cart == null || cart.getCartItems().isEmpty()) {
                throw new CartNotFoundException("Cart is empty or not found.");
            }

            // Prepare order items from cart
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart.getCartItems()) {
                Book book = dataStorage.getBookById(cartItem.getBookId());

                if (book == null || book.getStock() < cartItem.getCount()) {
                    throw new OutOfStockException("Not enough stock for book: "
                            + (book != null ? book.getTitle() : "Unknown"));
                }

                // Reduce the book count from stock
                book.setStock(book.getStock() - cartItem.getCount());
                dataStorage.updateBook(book.getId(), book);

                // Add book to order
                OrderItem orderItem = new OrderItem(book, cartItem.getCount());
                orderItems.add(orderItem);
            }

            // Create the new order
            Order newOrder = new Order(customerId, orderItems);
            dataStorage.saveOrder(newOrder);

            // Clear existing cart after order placement
            cart.getCartItems().clear();
            dataStorage.updateCart(cart);

            // Return successful order response
            return Response.status(Response.Status.CREATED)
                    .entity(newOrder)
                    .build();
        } catch (CustomerNotFoundException | CartNotFoundException | OutOfStockException e) {
            logger.error("Order placement failed: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Order placement failed.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error placing order", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while placing the order."))
                    .build();
        }
    }

    @Override
    public Response getOrders(String customerId) {
        try {
            // Check if the customer exists
            if (dataStorage.getCustomerById(customerId) == null) {
                throw new CustomerNotFoundException("Customer not found.");
            }

            List<Order> orders = dataStorage.getOrdersByCustomerId(customerId);
            if (orders.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("No orders found.", "No orders available for the given customer ID."))
                        .build();
            }

            return Response.ok(orders).build();
        } catch (CustomerNotFoundException e) {
            logger.error("Customer Not found", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Customer not found.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching orders", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while fetching the orders."))
                    .build();
        }
    }

    @Override
    public Response getOrderById(String customerId, String orderId) {
        try {
            // Check if the customer exists
            if (dataStorage.getCustomerById(customerId) == null) {
                throw new CustomerNotFoundException("Customer not found.");
            }

            Order order = dataStorage.getOrderById(customerId, orderId);
            if (order == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Order not found.", "No order found with the given ID for this customer."))
                        .build();
            }

            return Response.ok(order).build();
        } catch (CustomerNotFoundException e) {
            logger.error("Customer Not found", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Customer not found.", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching order", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while fetching the order."))
                    .build();
        }
    }

}
