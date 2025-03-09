package com.bookstore.model;

public class Customer extends Person {

    private String customerId;

    public Customer() {
        super("", "", "");
        this.customerId = "C" + System.currentTimeMillis(); 
    }

    public Customer(String name, String email, String password) {
        super(name, email, password);  
        this.customerId = "C" + System.currentTimeMillis();  
    }

    // Getter and Setter for customerId
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
