package com.example.demo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Project: demoDarbyFrameworks2-master
 * Package: com.example.demo.domain
 * <p>
 * User: carolyn.sher
 * Date: 6/24/2022
 * Time: 3:45 PM
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
class InhousePartTest {
    InhousePart ip;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ip = new InhousePart();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();  // Initialize the validator here
    }

    @Test
    void getPartId() {
        int idValue=4;
        ip.setPartId(idValue);
        assertEquals(ip.getPartId(), idValue);
    }

    @Test
    void setPartId() {
        int idValue=4;
        ip.setPartId(idValue);
        assertEquals(ip.getPartId(), idValue);
    }
    @Test
    void testConstructor() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // Truncate to seconds
        InhousePart ip = new InhousePart("Sample Part", 100.0, 10, 5, 20, now, 18865);
        Assertions.assertEquals("Sample Part", ip.getName());
        Assertions.assertEquals(100.0, ip.getPrice());
        Assertions.assertEquals(10, ip.getInv());
        Assertions.assertEquals(5, ip.getMin());
        Assertions.assertEquals(20, ip.getMax());
        Assertions.assertEquals(now, ip.getDateAdded().truncatedTo(ChronoUnit.SECONDS)); // Compare without nanoseconds
        Assertions.assertEquals(18865, ip.getStoreNumber());
    }
    @Test
    void testInvalidInventory() {
        ip.setInv(-10); // Negative inventory should fail
        Set<ConstraintViolation<InhousePart>> violations = validator.validate(ip);

        violations.forEach(violation -> {
            System.out.println("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
        });

        Assertions.assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("inv") &&
                        v.getMessage().contains("Inventory value must be positive") // Match the message in @Min annotation
        ), "Inventory should not be negative.");
    }

    @Test
    void testDateAddedAndStoreNumber() {
        LocalDateTime now = LocalDateTime.now();
        ip.setDateAdded(now);
        ip.setStoreNumber(1001);

        Assertions.assertEquals(now, ip.getDateAdded(), "DateAdded should match the assigned value.");
        Assertions.assertEquals(1001, ip.getStoreNumber(), "StoreNumber should match the assigned value.");
    }
    @Test
    void testPartId() {
        int partId = 1234;
        ip.setPartId(partId);
        Assertions.assertEquals(partId, ip.getPartId(), "Part ID should match the assigned value.");
    }



    @Test
    void testSettersAndGetters() {
        ip.setName("New Part Name");
        ip.setPrice(150.0);
        ip.setInv(15);
        ip.setMin(10);
        ip.setMax(25);

        assertEquals("New Part Name", ip.getName());
        assertEquals(150.0, ip.getPrice());
        assertEquals(15, ip.getInv());
        assertEquals(10, ip.getMin());
        assertEquals(25, ip.getMax());
    }
}