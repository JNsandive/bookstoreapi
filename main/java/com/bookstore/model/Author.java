package com.bookstore.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Author extends Person {

    private String biography;
    private String authorId;

    // Default constructor for Jackson
    public Author() {
        super("", "", "");  // Call Person constructor with default values
    }

    // Constructor with parameters
    @JsonCreator
    public Author(@JsonProperty("name") String name,
                  @JsonProperty("email") String email,
                  @JsonProperty("password") String password,
                  @JsonProperty("biography") String biography) {
        super(name, email, password);  // Call Person constructor with parameters
        this.biography = biography;
        this.authorId = "A" + System.currentTimeMillis(); // Generate unique authorId
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    // Getter and Setter for authorId
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
