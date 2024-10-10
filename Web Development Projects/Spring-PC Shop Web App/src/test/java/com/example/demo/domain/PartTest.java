package com.example.demo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PartTest {

    private Validator validator;
    private Part partIn;
    private Part partOut;

    @BeforeEach
    void setUp() {
        this.partIn = new InhousePart();
        this.partOut = new OutsourcedPart();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void getId() {
        Long idValue = 4L;
        partIn.setId(idValue);
        assertEquals(idValue, partIn.getId());
        partOut.setId(idValue);
        assertEquals(idValue, partOut.getId());
    }

    @Test
    void setId() {
        Long idValue = 4L;
        partIn.setId(idValue);
        assertEquals(idValue, partIn.getId());
        partOut.setId(idValue);
        assertEquals(idValue, partOut.getId());
    }

    @Test
    void getName() {
        String name = "test inhouse part";
        partIn.setName(name);
        assertEquals(name, partIn.getName());
        name = "test outsourced part";
        partOut.setName(name);
        assertEquals(name, partOut.getName());
    }

    @Test
    void setName() {
        String name = "test inhouse part";
        partIn.setName(name);
        assertEquals(name, partIn.getName());
        name = "test outsourced part";
        partOut.setName(name);
        assertEquals(name, partOut.getName());
    }

    @Test
    void getPrice() {
        double price = 1.0;
        partIn.setPrice(price);
        assertEquals(price, partIn.getPrice());
        partOut.setPrice(price);
        assertEquals(price, partOut.getPrice());
    }

    @Test
    void setPrice() {
        double price = 1.0;
        partIn.setPrice(price);
        assertEquals(price, partIn.getPrice());
        partOut.setPrice(price);
        assertEquals(price, partOut.getPrice());
    }

    @Test
    void getInv() {
        int inv = 5;
        partIn.setInv(inv);
        assertEquals(inv, partIn.getInv());
        partOut.setInv(inv);
        assertEquals(inv, partOut.getInv());
    }

    @Test
    void setInv() {
        int inv = 5;
        partIn.setInv(inv);
        assertEquals(inv, partIn.getInv());
        partOut.setInv(inv);
        assertEquals(inv, partOut.getInv());
    }

    @Test
    void getStoreNumber() {
        int storeNumber = 101;
        partIn.setStoreNumber(storeNumber);
        assertEquals(storeNumber, partIn.getStoreNumber());
        partOut.setStoreNumber(storeNumber);
        assertEquals(storeNumber, partOut.getStoreNumber());
    }

    @Test
    void setStoreNumber() {
        int storeNumber = 101;
        partIn.setStoreNumber(storeNumber);
        assertEquals(storeNumber, partIn.getStoreNumber());
        partOut.setStoreNumber(storeNumber);
        assertEquals(storeNumber, partOut.getStoreNumber());
    }

    @Test
    void getDateAdded() {
        LocalDateTime dateAdded = LocalDateTime.now();
        partIn.setDateAdded(dateAdded);
        assertEquals(dateAdded, partIn.getDateAdded());
        partOut.setDateAdded(dateAdded);
        assertEquals(dateAdded, partOut.getDateAdded());
    }

    @Test
    void setDateAdded() {
        LocalDateTime dateAdded = LocalDateTime.now();
        partIn.setDateAdded(dateAdded);
        assertEquals(dateAdded, partIn.getDateAdded());
        partOut.setDateAdded(dateAdded);
        assertEquals(dateAdded, partOut.getDateAdded());
    }

    @Test
    void getProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        Set<Product> myProducts = new HashSet<>();
        myProducts.add(product1);
        myProducts.add(product2);
        partIn.setProducts(myProducts);
        assertEquals(myProducts, partIn.getProducts());
        partOut.setProducts(myProducts);
        assertEquals(myProducts, partOut.getProducts());
    }

    @Test
    void setProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        Set<Product> myProducts = new HashSet<>();
        myProducts.add(product1);
        myProducts.add(product2);
        partIn.setProducts(myProducts);
        assertEquals(myProducts, partIn.getProducts());
        partOut.setProducts(myProducts);
        assertEquals(myProducts, partOut.getProducts());
    }

    @Test
    void testToString() {
        String name = "test inhouse part";
        partIn.setName(name);
        assertEquals(name, partIn.toString());
        name = "test outsourced part";
        partOut.setName(name);
        assertEquals(name, partOut.toString());
    }

    @Test
    void testEquals() {
        partIn.setId(1L);
        Part newPartIn = new InhousePart();
        newPartIn.setId(1L);
        assertEquals(partIn, newPartIn);
        partOut.setId(1L);
        Part newPartOut = new OutsourcedPart();
        newPartOut.setId(1L);
        assertEquals(partOut, newPartOut);
    }
    @Test
    void testNullName() {
        partIn.setName(null);

        Set<ConstraintViolation<Part>> violations = validator.validate(partIn);

        // Debug output
        violations.forEach(violation -> {
            System.out.println("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
        });
        Assertions.assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("name") &&
                        v.getMessage().contains("Name cannot be null")
        ), "Name should not be null.");
    }

    @Test
    void testInvalidPrice() {
        partIn.setPrice(-5.0); // Negative price should fail
        Set<ConstraintViolation<Part>> violations = validator.validate(partIn);

        // Debugging output to check violation messages
        violations.forEach(violation -> {
            System.out.println("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
        });

        Assertions.assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("price") &&
                        v.getMessage().contains("Price value must be positive") // Updated to match the actual message
        ), "Price should not be negative.");
    }


    @Test
    void testEmptyProductList() {
        partIn.setProducts(new HashSet<>());
        Assertions.assertTrue(partIn.getProducts().isEmpty(), "Product list should be empty.");
    }



    @Test
    void testHashCode() {
        partIn.setId(1L);
        partOut.setId(1L);
        assertEquals(partIn.hashCode(), partOut.hashCode());
    }

    // Test for inventory validation: min and max inventory checks
    @Test
    void testMinInventory() {
        // Create a Part instance with inventory below the minimum
        Part part = new InhousePart("Sample Part", 10.0, 4, 5, 10, LocalDateTime.now(), 1);

        // Validate the part object
        Set<ConstraintViolation<Part>> violations = validator.validate(part);

        // Check that the inventory value is below the minimum and thus should be invalid
        assertTrue(violations.stream().anyMatch(v ->
                        v.getPropertyPath().toString().equals("inv") &&
                                v.getMessage().equals("Cannot enter inventory outside of Min/Max range")),
                "Inventory should not go below the minimum value.");
    }

    @Test
    void testMaxInventory() {
        // Create a Part instance with inventory above the maximum
        Part part = new InhousePart("Sample Part", 10.0, 15, 5, 10, LocalDateTime.now(), 2);

        // Validate the part object
        Set<ConstraintViolation<Part>> violations = validator.validate(part);

        // Check that the inventory value exceeds the maximum and thus should be invalid
        assertTrue(violations.stream().anyMatch(v ->
                        v.getPropertyPath().toString().equals("inv") &&
                                v.getMessage().equals("Cannot enter inventory outside of Min/Max range")),
                "Inventory should not exceed the maximum value.");
    }
}
