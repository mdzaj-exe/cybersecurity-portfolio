package com.example.demo.validators;

import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PartValidator implements ConstraintValidator<ValidPart, Part> {

    @Override
    public void initialize(ValidPart constraintAnnotation) {
        // Initialization if needed
    }

    @Override
    public boolean isValid(Part part, ConstraintValidatorContext context) {
        // Check if part name is null or empty
        if (part.getName() == null || part.getName().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Part name cannot be null or empty")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            return false;
        }

        // If it's an OutsourcedPart, ensure the companyName is present
        if (part instanceof OutsourcedPart) {
            OutsourcedPart outsourcedPart = (OutsourcedPart) part;
            if (outsourcedPart.getCompanyName() == null || outsourcedPart.getCompanyName().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Company name must not be null or empty for Outsourced Part")
                        .addPropertyNode("companyName")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
