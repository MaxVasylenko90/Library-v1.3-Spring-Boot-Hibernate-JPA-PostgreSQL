package ua.vasylenko.library.v13.Spring.Boot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+", message = "Ex. \"Maksym Vasylenko\"")
    @NotEmpty(message = "The name can't be empty!")
    private String name;
    @Column(name = "year")
    @Min(value = 1900, message = "Year must be greater than 1900")
    @Max(value = 2024, message = "Year must be less than 2024")
    private int year;
    @Column(name = "email")
    @NotEmpty(message = "You should to specify your email!")
    private String email;
    @OneToMany(mappedBy = "person")
    private List<Book> books;

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public Person() {
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
