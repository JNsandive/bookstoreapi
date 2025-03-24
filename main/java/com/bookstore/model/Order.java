package com.bookstore.model;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class Order {

    private String orderId;
    @NotBlank(message = "Customer ID cannot be empty.")
    private String customerId;
    @NotEmpty(message = "Order must contain at least one item.")
    @Valid
    private List<OrderItem> orderItems;
    private String status;

    public Order(String customerId, List<OrderItem> orderItems) {
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.orderId = "ORDER" + System.currentTimeMillis(); // Generate unique order ID
        this.status = "Pending"; // Initial status of the order
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
