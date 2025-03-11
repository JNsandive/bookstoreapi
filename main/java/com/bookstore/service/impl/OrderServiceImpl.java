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

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public Response placeOrder(String customerId) {
        try {
            // Validate if customer exists
            if (dataStorage.getCustomerById(customerId) == null) {
                throw new CustomerNotFoundException("Customer not found.");
            }

            // Fetch the customer's cart
            Cart cart = dataStorage.getCustomerCart(customerId);
            if (cart == null) {
                throw new CartNotFoundException("Cart not found.");
            }

            // Prepare the list of order items from the cart
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart.getCartItems()) {
                int bookId = cartItem.getBookId();
                int count = cartItem.getCount();

                // Check if there's enough stock
                Book book = dataStorage.getBookById(bookId);
                if (book == null || book.getStock() < count) {
                    throw new OutOfStockException("Not enough stock for book: " + book.getTitle());
                }

                // Create OrderItem and add it to the order
                OrderItem orderItem = new OrderItem(book, count);
                orderItems.add(orderItem);
            }

            // Create an order with the prepared order items
            Order newOrder = new Order(customerId, orderItems);

            // Save the order in the data storage
            dataStorage.saveOrder(newOrder);

            // clear the cart items and update the cart.
            cart.getCartItems().clear();
            dataStorage.updateCart(cart); 

            // Return success response with order ID and order details
            return Response.status(Response.Status.CREATED)
                    .entity(newOrder)
                    .build();
        } catch (CustomerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (CartNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (OutOfStockException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            // Catch any other unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while placing the order.")
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
                        .entity("No orders found for the customer.")
                        .build();
            }

            return Response.ok(orders).build();
        } catch (CustomerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching the orders.")
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
                        .entity("Order not found.")
                        .build();
            }

            return Response.ok(order).build();
        } catch (CustomerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching the order.")
                    .build();
        }
    }
}
