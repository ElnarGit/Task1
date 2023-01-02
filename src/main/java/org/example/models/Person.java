package org.example.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Person {
    private int id;

    @NotEmpty(message = "Name should be empty")
    @Size(min = 2, max =  50, message = "Name should not between 2 and 50 characters")
    private String FIO;

    @Min(value = 1900, message = "Year of Birth should be greater than 1900")
    private int year;

    public Person(){}

    public Person(int id, String FIO, int year) {
        this.id = id;
        this.FIO = FIO;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
