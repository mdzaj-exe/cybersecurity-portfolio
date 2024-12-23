package com.example.demo.repositories;

import com.example.demo.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> { //usage of extends--> inhertiance
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    List<Product> search(String keyword);
}
//Example of using Spring Data JPA, which comes with security features and is used to access databases.
//ProductRepository is an example of abstracting away data access