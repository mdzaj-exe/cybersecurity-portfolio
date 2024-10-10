package com.example.demo.service;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
public class ReportService {

    public void generatePartReport(List<Part> parts, PrintWriter writer) {
        writer.println("Part Report");
        writer.println("ID,Name,Price,Inventory,Date Added,Store Number");
        for (Part part : parts) {
            writer.println(part.getId() + "," +
                    part.getName() + "," +
                    part.getPrice() + "," +
                    part.getInv() + "," +
                    part.getDateAdded().toString() + "," +
                    part.getStoreNumber());
        }
    }

    public void generateProductReport(List<Product> products, PrintWriter writer) {
        writer.println("Product Report");
        writer.println("ID,Name,Price,Inventory,Date Added,Store Number");
        for (Product product : products) {
            writer.println(product.getId() + "," +
                    product.getName() + "," +
                    product.getPrice() + "," +
                    product.getInv() + "," +
                    product.getDateAdded().toString() + "," +
                    product.getStoreNumber());
        }
    }
}
