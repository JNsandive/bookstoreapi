package com.bookstore.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class Person {

    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email cannot be empty.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    private String password;

    public Person() {
    }

    public Person(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
