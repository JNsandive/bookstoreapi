package com.bookstore.resource;

import com.bookstore.model.Customer;
import com.bookstore.service.CustomerService;
import com.bookstore.service.impl.CustomerServiceImpl;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
public class CustomerResource {

    private CustomerService customerService = new CustomerServiceImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(@Valid Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerById(@PathParam("customerId") String customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PUT
    @Path("/{customerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("customerId") String customerId,@Valid Customer customer) {
        return customerService.updateCustomer(customerId, customer);
    }

    @DELETE
    @Path("/{customerId}")
    public Response deleteCustomer(@PathParam("customerId") String customerId) {
        return customerService.deleteCustomer(customerId);
    }
}
