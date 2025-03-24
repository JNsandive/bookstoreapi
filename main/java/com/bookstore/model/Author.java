package com.bookstore.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

public class Author extends Person {

    @NotBlank(message = "Biography cannot be empty.")
    private String biography;
    private String authorId;

    // Default constructor for Jackson
    public Author() {
        super("", "", "");
    }

    // Constructor with parameters
    @JsonCreator
    public Author(@JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("biography") String biography) {
        super(name, email, password);
        this.biography = biography;
        this.authorId = "A" + System.currentTimeMillis(); // Generate unique authorId
    }

    // Getter and Setters
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
