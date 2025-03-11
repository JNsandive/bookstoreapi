package com.bookstore.resource;

import com.bookstore.model.Order;
import com.bookstore.service.OrderService;
import com.bookstore.service.impl.OrderServiceImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private OrderService orderService = new OrderServiceImpl();

    // Place a new order
    @POST
    public Response placeOrder(@PathParam("customerId") String customerId) {
        return orderService.placeOrder(customerId);
    }

    // Get all orders for a customer
    @GET
    public Response getOrders(@PathParam("customerId") String customerId) {
        return orderService.getOrders(customerId);
    }

    // Get a specific order by order ID
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") String customerId,
                                  @PathParam("orderId") String orderId) {
        return orderService.getOrderById(customerId, orderId);
    }
}
