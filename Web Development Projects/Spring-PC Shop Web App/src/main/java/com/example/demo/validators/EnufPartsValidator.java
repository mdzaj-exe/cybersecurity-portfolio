package com.example.demo.validators;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnufPartsValidator implements ConstraintValidator<ValidEnufParts, Product> { //usage of implements--> polymorphism
    @Autowired
    private ApplicationContext context;
    private static ApplicationContext myContext;

    @Override
    public void initialize(ValidEnufParts constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Product product, ConstraintValidatorContext constraintValidatorContext) {
        if (context == null) return true;
        if (context != null) myContext = context;

        ProductService repo = myContext.getBean(ProductServiceImpl.class);

        if (product.getId() != 0) {
            // Fetch existing product from database
            Product existingProduct = repo.findById((int) product.getId());

            // Check if increasing the product inventory is valid
            int inventoryChange = product.getInv() - existingProduct.getInv();
            if (inventoryChange > 0) {
                for (Part part : existingProduct.getParts()) {
                    if (part.getInv() - inventoryChange < 0) {
                        // Adding inventory would cause a part to have negative inventory
                        return false;
                    }
                }
            }

            // Validate each part associated with the product
            for (Part part : existingProduct.getParts()) {
                if (product.getInv() > part.getInv()) {
                    // Product inventory exceeds the part inventory
                    return false;
                }
                if (part.getInv() < part.getMin() || part.getInv() > part.getMax()) {
                    // Part inventory is out of valid range
                    return false;
                }
            }
        }

        return true;
    }
}
