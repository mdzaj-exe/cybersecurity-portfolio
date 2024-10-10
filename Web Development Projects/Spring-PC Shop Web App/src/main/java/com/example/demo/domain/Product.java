package com.example.demo.domain;

import com.example.demo.validators.ValidEnufParts;
import com.example.demo.validators.ValidProductPrice;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="Products")
@ValidProductPrice
@ValidEnufParts
public class Product implements Serializable { //usage of implements--> polymorphism
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "Product name cannot be null")
    @Size(min = 1, message = "Name must have at least 1 character")
    private String name;

    @Min(value = 0, message = "Price value must be positive")
    private double price;

    @Min(value = 1, message = "Inventory value must be positive")
    @Max(value = 999, message = "Inventory cannot exceed 999")
    private int inv;


    private LocalDateTime dateAdded; //examples of encapsulation

    @Min(value = 0, message = "storeNumber must be positive")
    private int storeNumber;


    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "products", fetch = FetchType.LAZY) //Lazy Loading with JPA--> entity only fetched when needed
    private Set<Part> parts = new HashSet<>();


    public Product() {
    }

    public Product(String name, double price, int inv) {
        this.name = name;
        this.price = price;
        this.inv = inv;
    }

    public Product(long id, String name, double price, int inv, LocalDateTime dateAdded, int storeNumber) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.dateAdded = dateAdded;
        this.storeNumber = storeNumber;
    }

    public Product(String name, double price, int inv, LocalDateTime dateAdded, int storeNumber) {
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.dateAdded = dateAdded;
        this.storeNumber = storeNumber;
    }
    // Automatically set the dateAdded before the entity is persisted
    @PrePersist
    protected void onCreate() {
        if (dateAdded == null) {
            this.dateAdded = LocalDateTime.now();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInv() {
        return inv;
    }

    public void setInv(int inv) {
        this.inv = inv;
    }

    public Set<Part> getParts() {
        return parts;
    }

    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }

    public String toString(){
        return this.name;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id == product.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
