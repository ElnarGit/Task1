package org.example.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Book {
    private int id;

    @NotEmpty
    @Size(min = 2, max = 50, message = "Book should not between 2 and 50 characters")
    private String name;

    @NotEmpty
    @Size(min = 2, max = 20, message = "Author name should not between 2 and 20 characters")
    private String author;

    @Min(value = 1500, message = "Year should be greater than 1500")
    private int year;

    public Book(){}

    public Book(int id, String name, String author, int year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
