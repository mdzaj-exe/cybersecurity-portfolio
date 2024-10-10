package com.example.demo.validators;

import com.example.demo.domain.Part;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InventoryRangeValidator implements ConstraintValidator<ValidInventoryRange, Part> { //validation functionality for inventory range

    @Override
    public void initialize(ValidInventoryRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(Part part, ConstraintValidatorContext context) {
        if (part.getMin() > part.getMax()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Max must be greater than Min")
                    .addPropertyNode("max")
                    .addConstraintViolation();
            return false;
        }
        if (part.getInv() < part.getMin() || part.getInv() > part.getMax()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Cannot enter inventory outside of Min/Max range")
                    .addPropertyNode("inv")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
