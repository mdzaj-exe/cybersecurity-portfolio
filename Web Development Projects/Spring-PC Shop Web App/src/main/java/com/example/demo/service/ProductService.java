package com.example.demo.service;

import com.example.demo.domain.Product;

import java.util.List;

/**
 *
 *
 *
 *
 */
public interface ProductService {
    public List<Product> findAll();
    public Product findById(int theId);
    public Product save (Product theProduct);
    public void deleteById(int theId); //functionality to delete a product from the database
    public List<Product> listAll(String keyword);
    boolean isProductListEmpty();
    void addSampleProducts();
    List<Product> findByName(String name);

}
