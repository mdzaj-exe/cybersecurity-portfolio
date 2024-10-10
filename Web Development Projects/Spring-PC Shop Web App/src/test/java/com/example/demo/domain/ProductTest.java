package com.example.demo.domain;

import com.example.demo.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;


class ProductTest {
    Product product;
    private Validator validator;

    @Mock
    private ProductService productService;  // Mocking ProductService

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);  // Initialize mocks

        product = new Product();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void getId() {
        Long idValue=4L;
        product.setId(idValue);
        assertEquals(product.getId(), idValue);
    }

    @Test
    void setId() {
        Long idValue=4L;
        product.setId(idValue);
        assertEquals(product.getId(), idValue);
    }

    @Test
    void getName() {
        String name="test product";
        product.setName(name);
        assertEquals(name,product.getName());
    }

    @Test
    void setName() {
        String name="test product";
        product.setName(name);
        assertEquals(name,product.getName());
    }

    @Test
    void getPrice() {
        double price=1.0;
        product.setPrice(price);
        assertEquals(price,product.getPrice());
    }

    @Test
    void setPrice() {
        double price=1.0;
        product.setPrice(price);
        assertEquals(price,product.getPrice());
    }

    @Test
    void getInv() {
        int inv=5;
        product.setInv(inv);
        assertEquals(inv,product.getInv());
    }

    @Test
    void setInv() {
        int inv=5;
        product.setInv(inv);
        assertEquals(inv,product.getInv());
    }

    @Test
    void getParts() {
        Part part1 = new OutsourcedPart();
        Part part2 = new InhousePart();
        Set<Part> myParts= new HashSet<>();
        myParts.add(part1);
        myParts.add(part2);
        product.setParts(myParts);
        assertEquals(myParts,product.getParts());
    }

    @Test
    void setParts() {
        Part part1 = new OutsourcedPart();
        Part part2 = new InhousePart();
        Set<Part> myParts= new HashSet<>();
        myParts.add(part1);
        myParts.add(part2);
        product.setParts(myParts);
        assertEquals(myParts,product.getParts());
    }

    @Test
    void testToString() {
        String name="test product";
        product.setName(name);
        assertEquals(name,product.toString());
    }

    @Test
    void testEquals() {
        product.setId(1l);
        Product newProduct= new Product();
        newProduct.setId(1l);
        assertEquals(product,newProduct);
    }

    @Test
    void testHashCode() {
        product.setId(1l);
        Product newProduct= new Product();
        newProduct.setId(1l);
        assertEquals(product.hashCode(),newProduct.hashCode());
    }
    @Test
    void testNullName() {
        product.setName(null);  // Set name to null
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        violations.forEach(v -> {
            System.out.println("Violation: " + v.getPropertyPath() + " - " + v.getMessage());
        });

        Assertions.assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("name") &&
                        v.getMessage().contains("Product name cannot be null")
        ), "Product name should not be null.");
    }

    @Test
    void testInventoryOutsideRange() {
        product.setInv(1000); // Exceeds maximum allowed inventory
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        violations.forEach(v -> {
            System.out.println("Violation: " + v.getPropertyPath() + " - " + v.getMessage());
        });

        Assertions.assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("inv") &&
                        v.getMessage().contains("Inventory cannot exceed 999")  // Ensure this matches the actual message
        ), "Inventory should not exceed the allowed maximum.");
    }


    @Test
    void testDateAddedAutomaticallySet() {
        // Create a new Product instance (simulate creating a new product)
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setInv(10);
        product.setStoreNumber(123);

        // Validate the product
        Set<ConstraintViolation<Product>> violations = this.validator.validate(product);

        // Check if there are any violations (there should be none related to dateAdded being null)
        Assertions.assertTrue(violations.isEmpty(), "There should be no violations.");

        // Manually trigger the @PrePersist lifecycle event
        product.onCreate();

        // Simulate saving the product (no need to mock here, we're just testing the date)
        Assertions.assertNotNull(product.getDateAdded(), "DateAdded should be automatically set before saving.");
    }




    @Test
    void testAddPart() {
        Part part = new InhousePart();
        product.getParts().add(part);
        Assertions.assertTrue(product.getParts().contains(part), "Product should contain the added part.");
    }



}