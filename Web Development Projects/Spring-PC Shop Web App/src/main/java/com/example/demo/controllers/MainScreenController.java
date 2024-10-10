package com.example.demo.controllers;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.PartService;
import com.example.demo.service.ProductService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 *
 *
 *
 *
 */

@Controller
public class MainScreenController {

    private PartService partService;
    private ProductService productService;

    public MainScreenController(PartService partService, ProductService productService) {
        this.partService = partService;
        this.productService = productService;
    }

    @GetMapping("/mainscreen")
    public String listPartsAndProducts(Model theModel, @Param("partkeyword") String partkeyword, @Param("productkeyword") String productkeyword, @Param("errorMessage") String errorMessage) {
        List<Part> partList = partService.listAll(partkeyword);
        theModel.addAttribute("parts", partList);
        theModel.addAttribute("partkeyword", partkeyword);

        List<Product> productList = productService.listAll(productkeyword);
        theModel.addAttribute("products", productList);
        theModel.addAttribute("productkeyword", productkeyword);

        // Add error message
        theModel.addAttribute("errorMessage", errorMessage);

        return "mainscreen";
    }

    @GetMapping("/addSampleDataIfEmpty")
    public String addSampleDataIfEmpty() {
        if (partService.isPartListEmpty() && productService.isProductListEmpty()) {
            try {
                partService.addSampleParts();
                productService.addSampleProducts();
            } catch (ConstraintViolationException e) {
                return "redirect:/mainscreen?errorMessage=Combined Inventory Out of Range";
            }
        }
        return "redirect:/mainscreen";
    }

    @GetMapping("/addSampleData")
    public String addSampleData() {
        try {
            partService.addSampleParts();
            productService.addSampleProducts();
        } catch (ConstraintViolationException e) {
            return "redirect:/mainscreen?errorMessage=Combined Inventory Out of Range";
        }
        return "redirect:/mainscreen";
    }
}
