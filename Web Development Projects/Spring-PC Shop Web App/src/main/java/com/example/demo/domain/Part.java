package com.example.demo.domain;

import com.example.demo.validators.ValidDeletePart;
import com.example.demo.validators.ValidInventoryRange;
import com.example.demo.validators.ValidPart;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@ValidDeletePart
@ValidPart
@ValidInventoryRange
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "part_type", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "Parts")
public class Part implements Serializable { //usage of implements--> polymorphism
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, message = "Name must have at least 1 character")//Usage of Java Bean Validation
    String name;

    @Min(value = 0, message = "Price value must be positive") //Usage of Java Bean Validation
    double price;

    @Min(value = 0, message = "Inventory value must be positive")
    @Max(value = 10000, message = "Inventory cannot exceed 10000")
    int inv;

    @Min(value = 0, message = "Minimum inventory value must be positive")
    int min;

    @Max(value = 10000, message = "Maximum inventory value must not exceed 10000") //Usage of Java Bean Validation
    int max;

    private LocalDateTime dateAdded;
    //examples of encapsulation
    @Min(value = 0, message = "Store number must be greater than or equal to 0")
    private int storeNumber;


    @ManyToMany
    @JoinTable(name = "product_part", joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

    public Part() {
    }

    public Part(long id, String name, double price, int inv, int min, int max, LocalDateTime dateAdded, int storeNumber) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.min = min;
        this.max = max;
        this.dateAdded = dateAdded;
        this.storeNumber = storeNumber;
    }

    public Part(long id, String name, double price, int inv, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.min = min;
        this.max = max;
    }

    public Part(String name, double price, int inv, int min, int max, LocalDateTime dateAdded, int storeNumber) {
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.min = min;
        this.max = max;
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

    @Override
    public String toString() {
        return name;
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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Part part = (Part) o;

        return id == part.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
