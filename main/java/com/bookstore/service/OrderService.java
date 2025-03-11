package com.bookstore.service;

import javax.ws.rs.core.Response;

public interface OrderService {

    Response placeOrder(String customerId);
    Response getOrders(String customerId);
    Response getOrderById(String customerId, String orderId);
}
