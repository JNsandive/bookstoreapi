package com.bookstore.service.impl;

import com.bookstore.model.Customer;
import com.bookstore.model.ErrorResponse;
import com.bookstore.util.DataStorage;
import com.bookstore.service.CustomerService;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private DataStorage dataStorage = DataStorage.getInstance();
    private Validator validator;

    public CustomerServiceImpl() {
        // Initialize validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Create Customer Method
    @Override
    public Response createCustomer(Customer customer) {
        try {
            // Validate the customer object
            Set<javax.validation.ConstraintViolation<Customer>> violations = validator.validate(customer);
            if (!violations.isEmpty()) {
                // Collect the error messages and return a BAD_REQUEST response
                String errorMessage = violations.stream()
                        .map(violation -> violation.getMessage())
                        .collect(Collectors.joining(", "));
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Validation error.", errorMessage))
                        .build();
            }

            // Add customer if valid
            Customer createdCustomer = dataStorage.addCustomer(customer);
            return Response.status(Response.Status.CREATED).entity(createdCustomer).build();

        } catch (Exception e) {
            logger.error("Unexpected error while creating customer: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while processing the request."))
                    .build();
        }
    }

    // Get Customer Details Method
    @Override
    public Response getCustomerById(String customerId) {
        try {
            Customer customer = dataStorage.getCustomerById(customerId);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Customer not found.", "No customer found with the given ID."))
                        .build();
            }
            return Response.ok(customer).build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching customer: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while fetching the customer."))
                    .build();
        }
    }

    // Get All Customer Method
    @Override
    public Response getAllCustomers() {
        try {
            return Response.ok(dataStorage.getCustomers()).build();
        } catch (Exception e) {
            logger.error("An error occurred while fetching all customers: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while fetching customers."))
                    .build();
        }
    }

    // Update Customer Method
    @Override
    public Response updateCustomer(String customerId, Customer customer) {
        try {
            Set<javax.validation.ConstraintViolation<Customer>> violations = validator.validate(customer);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(violation -> violation.getMessage())
                        .collect(Collectors.joining(", "));
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Validation error.", errorMessage))
                        .build();
            }

            Customer updatedCustomer = dataStorage.updateCustomer(customerId, customer);
            if (updatedCustomer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Customer not found.", "No customer found with the given ID."))
                        .build();
            }
            return Response.ok(updatedCustomer).build();
        } catch (Exception e) {
            logger.error("An error occurred while updating customer: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while updating the customer."))
                    .build();
        }
    }

    // Delete Customer Method
    @Override
    public Response deleteCustomer(String customerId) {
        try {
            boolean success = dataStorage.removeCustomer(customerId);
            if (!success) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Customer not found.", "No customer found with the given ID."))
                        .build();
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            logger.error("An error occurred while deleting customer: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error.", "An error occurred while deleting the customer."))
                    .build();
        }
    }

}
