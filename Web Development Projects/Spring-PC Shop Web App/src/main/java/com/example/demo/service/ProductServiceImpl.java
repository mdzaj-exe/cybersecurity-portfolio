package com.example.demo.service;

import com.example.demo.domain.Product;
import com.example.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 *
 *
 *
 */
@Service
public class ProductServiceImpl implements ProductService { //usage of implements--> polymorphism
    private ProductRepository productRepository;

    @Autowired

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.search(name);
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product findById(int theId) {
        if (theId <= 0) {
            throw new IllegalArgumentException("Invalid product ID: " + theId);
        }

        Long theIdl = (long) theId;
        Optional<Product> result = productRepository.findById(theIdl);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Did not find product id - " + theId);
        }
    }



/*
    @Override
    public void save(Product theProduct) {
        productRepository.save(theProduct);

    }
    */

    @Transactional
    public Product save(Product product) { //functionality to save the part to the database
        if (product.getId() != 0) {
            return productRepository.saveAndFlush(product);
        } else {
            return productRepository.save(product);
        }
    }



    @Override
    public void deleteById(int theId) { //functionality to delete a product from the database
        Long theIdl = (long) theId;
        productRepository.deleteById(theIdl);
    }

    public List<Product> listAll(String keyword) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public boolean isProductListEmpty() {
        return productRepository.count() == 0;
    }

    @Override
    public void addSampleProducts() {
        // Sample data for products
        List<Product> sampleProducts = List.of(
                new Product("Gaming PC Build", 1499.99, 5, LocalDateTime.now(), 123),
                new Product("Workstation PC", 1299.99, 8, LocalDateTime.now(), 11),
                new Product("Entry-Level Gaming PC", 799.99, 10, LocalDateTime.now(), 53),
                new Product("High-End Laptop", 1799.99, 6, LocalDateTime.now(), 87),
                new Product("Business Laptop", 1199.95, 12, LocalDateTime.now(), 224)
        );

        for (Product sampleProduct : sampleProducts) {
            // Check for existing products with the same name
            List<Product> existingProducts = productRepository.search(sampleProduct.getName());

            if (existingProducts.isEmpty()) {
                // No existing product with same name, adds product
                productRepository.save(sampleProduct);
            } else {
                // Handle dupes by creating or updating a multi-pack
                Product existingProduct = existingProducts.get(0); // Assume no exact duplicates, pick the first match

                // multi-pack name gen
                String multiPackName = existingProduct.getName() + " - Multi-Pack";
                int combinedQuantity = existingProduct.getInv() + sampleProduct.getInv();
                double combinedPrice = (existingProduct.getPrice() * existingProduct.getInv() + sampleProduct.getPrice() * sampleProduct.getInv()) / combinedQuantity;

                // Check if multi-pack  exists
                List<Product> existingMultiPacks = productRepository.search(multiPackName);
                if (existingMultiPacks.isEmpty()) {
                    // Save new
                    Product multiPackProduct = new Product(multiPackName, combinedPrice, combinedQuantity);
                    productRepository.save(multiPackProduct);
                } else {
                    // Update existing
                    Product existingMultiPack = existingMultiPacks.get(0);
                    existingMultiPack.setInv(existingMultiPack.getInv() + sampleProduct.getInv());
                    existingMultiPack.setPrice((existingMultiPack.getPrice() * existingMultiPack.getInv() + sampleProduct.getPrice() * sampleProduct.getInv()) / (existingMultiPack.getInv() + sampleProduct.getInv()));
                    productRepository.save(existingMultiPack); //functionality to save the part to the database
                }
            }
        }
    }


}
