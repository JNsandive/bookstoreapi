package com.bookstore.service;

import com.bookstore.model.Customer;
import javax.ws.rs.core.Response;

public interface CustomerService {

    Response createCustomer(Customer customer);
    Response getAllCustomers();
    Response getCustomerById(String customerId);
    Response updateCustomer(String customerId, Customer customer);
    Response deleteCustomer(String customerId);
}
