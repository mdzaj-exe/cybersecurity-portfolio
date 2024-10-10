package com.example.demo.repositories;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> { //usage of extends--> inhertiance
    @Query("SELECT p FROM Part p WHERE p.name LIKE %?1%")
    List<Part> search(String keyword); //Search method
}
//Example of using Spring Data JPA, which comes with security features and is used to access databases.
//ProductRepository is an example of abstracting away data access