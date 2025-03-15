package com.bookstore.service;

import com.bookstore.dao.CartItemRequest;
import javax.ws.rs.core.Response;

public interface CartService {
    
    Response addItemToCart(String customerId, CartItemRequest cartItemRequest);
    Response getCartItems(String customerId);
    Response updateCartItem(String customerId, int bookId, CartItemRequest cartItemRequest);
    Response removeItemFromCart(String customerId, int bookId);
}
