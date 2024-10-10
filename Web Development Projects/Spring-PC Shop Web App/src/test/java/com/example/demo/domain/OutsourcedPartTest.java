package com.example.demo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Project: demoDarbyFrameworks2-master
 * Package: com.example.demo.domain
 * <p>
 * User: carolyn.sher
 * Date: 6/24/2022
 * Time: 3:44 PM
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
class OutsourcedPartTest {

    OutsourcedPart op;
    private Validator validator;

    @BeforeEach
    void setUp() {
        op = new OutsourcedPart();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();  // Initialize the validator here
    }

    @Test
    void getCompanyName() {
        String name="test company name";
        op.setCompanyName(name);
        assertEquals(name,op.getCompanyName());
    }

    @Test
    void setCompanyName() {
        String name="test company name";
        op.setCompanyName(name);
        assertEquals(name,op.getCompanyName());
    }
    @Test
    void testNullCompanyName() {
        op.setCompanyName(null);
        Set<ConstraintViolation<OutsourcedPart>> violations = validator.validate(op);
        Assertions.assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("companyName") &&
                        v.getMessage().contains("must not be null")
        ), "Company name should not be null.");
    }
    @Test
    void testInvalidStoreNumber() {
        op.setStoreNumber(-1); // Assuming store number must be positive
        Set<ConstraintViolation<OutsourcedPart>> violations = validator.validate(op);
        Assertions.assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("storeNumber") &&
                        v.getMessage().contains("must be greater than or equal to 0")
        ), "Store number should not be negative.");
    }


}