package com.example.demo.controllers;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.PartService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing products
 */
@Controller
public class AddProductController {

    private final PartService partService;
    private final ProductService productService;
    private static Product product1;
    private Product product;
    private final ReportService reportService;


    @Autowired
    private EntityManager entityManager;

    @Autowired
    public AddProductController(PartService partService, ProductService productService, ReportService reportService) {
        this.partService = partService;
        this.productService = productService;
        this.reportService = reportService;
    }

    @GetMapping("/showFormAddProduct")
    public String showFormAddProduct(Model theModel) {
        theModel.addAttribute("parts", partService.findAll());
        product = new Product();
        product1 = product;
        theModel.addAttribute("product", product);

        List<Part> availParts = new ArrayList<>();
        for (Part p : partService.findAll()) {
            if (!product.getParts().contains(p)) availParts.add(p);
        }
        theModel.addAttribute("availableParts", availParts);
        theModel.addAttribute("associatedParts", product.getParts());
        return "productForm";
    }

    @PostMapping("/showFormAddProduct")
    public String submitForm(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model theModel) {
        if (bindingResult.hasErrors()) {
            theModel.addAttribute("parts", partService.findAll());
            List<Part> availParts = new ArrayList<>();
            for (Part p : partService.findAll()) {
                if (!product.getParts().contains(p)) availParts.add(p);
            }
            theModel.addAttribute("availableParts", availParts);
            theModel.addAttribute("associatedParts", product.getParts());
            return "productForm";
        } else {
            System.out.println("Handling product submission: ID = " + product.getId() + ", Name = " + product.getName());

            // Automatically set the dateAdded if it's a new product
            if (product.getId() == 0) {
                product.setDateAdded(LocalDateTime.now());  // Set the dateAdded to the current date
            }

            if (product.getId() > 0) {
                // Update existing product
                updateAssociatedParts(product); // Functionality to properly updated
                productService.save(product);
                System.out.println("Updated existing product with ID: " + product.getId());
            } else {
                // Handle new product creation
                List<Product> existingProducts = productService.findByName(product.getName());

                if (!existingProducts.isEmpty()) {
                    String uniqueName = product.getName() + " - Multi-Pack";
                    product.setName(uniqueName);
                    updateAssociatedParts(product);  // Ensure parts are properly updated
                    productService.save(product);
                    System.out.println("Created new product with unique name: " + product.getName());
                } else {
                    updateAssociatedParts(product);  // Ensure parts are properly updated
                    productService.save(product);
                    System.out.println("Saved new product: ID = " + product.getId() + ", Name = " + product.getName());
                }
            }
            return "confirmationaddproduct";
        }
    }









    @GetMapping("/showProductFormForUpdate")
    public String showProductFormForUpdate(@RequestParam("productID") int theId, Model theModel) {
        theModel.addAttribute("parts", partService.findAll());
        Product theProduct = productService.findById(theId);
        product1 = theProduct;

        // debugging
        System.out.println("Loading product for update with ID: " + theProduct.getId() + " and name: " + theProduct.getName());
        if (theProduct.getParts() != null && !theProduct.getParts().isEmpty()) {
            System.out.println("Associated parts for product ID " + theProduct.getId() + ":");
            for (Part part : theProduct.getParts()) {
                System.out.println("- Part ID: " + part.getId() + ", Name: " + part.getName() + ", Inventory: " + part.getInv());
            }
        } else {
            System.out.println("No associated parts for product ID " + theProduct.getId());
        }

        theModel.addAttribute("product", theProduct);
        theModel.addAttribute("associatedParts", theProduct.getParts());

        List<Part> availableParts = new ArrayList<>();
        for (Part p : partService.findAll()) {
            if (!theProduct.getParts().contains(p)) {
                availableParts.add(p);
            }
        }
        theModel.addAttribute("availableParts", availableParts);

        return "productForm";
    }

    @GetMapping("/deleteproduct") //functionality to delete a product
    public String deleteProduct(@RequestParam("productID") int theId, Model theModel) {
        Product product2 = productService.findById(theId);
        if (product2 != null) {
            for (Part part : product2.getParts()) {
                part.getProducts().remove(product2);
                partService.save(part);
            }
            product2.getParts().clear();
            productService.deleteById(theId);
        }

        return "confirmationdeleteproduct";
    }


    @GetMapping("/associatepart")
    public String associatePart(@RequestParam("partID") int theID, Model theModel) {
        if (product1 == null || product1.getName() == null) {
            return "redirect:/showFormAddProduct";
        }

        Part partToAssociate = partService.findById(theID);
        if (partToAssociate != null) {
            System.out.println("Associating part ID: " + theID + " with product ID: " + product1.getId());


            if (partToAssociate.getInv() <= product1.getInv()) {
                theModel.addAttribute("error", "Cannot add part. Adding this part would cause its inventory to drop below the product inventory.");
                return setupProductForm(theModel, product1);
            }


            int newQuantity = partToAssociate.getInv() - 1;
            if (newQuantity < partToAssociate.getMin() || newQuantity > partToAssociate.getMax()) {
                theModel.addAttribute("error", "Cannot add part. Inventory will be out of range.");
                return setupProductForm(theModel, product1);
            }


            product1.getParts().add(partToAssociate);
            partToAssociate.getProducts().add(product1);
            partToAssociate.setInv(newQuantity);
            partService.save(partToAssociate);

            productService.save(product1);
        }

        return setupProductForm(theModel, product1);
    }





    @GetMapping("/removepart")
    public String removePart(@RequestParam("partID") int theID, Model theModel) {
        Part partToRemove = partService.findById(theID);
        int newQuantity = partToRemove.getInv() + 1;


        if (newQuantity < partToRemove.getMin() || newQuantity > partToRemove.getMax()) {
            theModel.addAttribute("error", "Cannot remove part. Inventory will be out of range.");
            theModel.addAttribute("product", product1);
            theModel.addAttribute("associatedParts", product1.getParts());
            List<Part> availableParts = new ArrayList<>();
            for (Part p : partService.findAll()) {
                if (!product1.getParts().contains(p)) {
                    availableParts.add(p);
                }
            }
            theModel.addAttribute("availableParts", availableParts);
            return "productForm";
        }

        product1.getParts().remove(partToRemove);
        partToRemove.getProducts().remove(product1);
        partToRemove.setInv(newQuantity);
        partService.save(partToRemove);

        productService.save(product1);

        theModel.addAttribute("product", product1);
        theModel.addAttribute("associatedParts", product1.getParts());
        List<Part> availableParts = new ArrayList<>();
        for (Part p : partService.findAll()) {
            if (!product1.getParts().contains(p)) {
                availableParts.add(p);
            }
        }
        theModel.addAttribute("availableParts", availableParts);

        return "productForm";
    }

    @PostMapping("/submitProduct")
    public String submitProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model theModel) {
        System.out.println("Attempting to submit product form.");
        System.out.println("Handling product submission: ID = " + product.getId() + ", Name = " + product.getName());

        // Log binding errors if any
        if (bindingResult.hasErrors()) {
            System.out.println("Errors found in product form submission: ");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            theModel.addAttribute("parts", partService.findAll());
            List<Part> availParts = new ArrayList<>();
            for (Part p : partService.findAll()) {
                if (!product.getParts().contains(p)) availParts.add(p);
            }
            theModel.addAttribute("availableParts", availParts);
            theModel.addAttribute("associatedParts", product.getParts());
            return "productForm";
        }

        // Auto-set dateAdded for new products
        if (product.getId() == 0) {
            product.setDateAdded(LocalDateTime.now());
            System.out.println("Auto-setting dateAdded for new product: " + product.getDateAdded());
        } else {
            System.out.println("Updating existing product: " + product.getName());
        }

        updateAssociatedParts(product);
        productService.save(product);
        return "confirmationaddproduct";
    }




//    @Transactional //Example of Spring Framework's Transaction Management
//    @PostMapping("/updateProduct") //functionality to update a product
//    public String updateProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model) {
//        System.out.println("updateProduct method triggered.");
//
//        if (!bindingResult.hasErrors()) {
//            int productId = (int) product.getId();
//            Product existingProduct = productService.findById(productId);
//            int inventoryChange = product.getInv() - existingProduct.getInv();
//
//            if (inventoryChange > 0) { // If inventory is increasing
//                System.out.println("Increasing product inventory by: " + inventoryChange);
//
//
//                boolean validInventoryChange = true;
//                for (Part part : existingProduct.getParts()) {
//                    int newPartInventory = part.getInv() - inventoryChange;
//                    if (newPartInventory < part.getMin()) {
//                        validInventoryChange = false;
//                        model.addAttribute("error", "Cannot update product inventory. Would result in invalid inventory for part ID: " + part.getId());
//                        return setupProductForm(model, existingProduct);
//                    }
//                }
//
//
//                if (validInventoryChange) {
//                    for (Part part : existingProduct.getParts()) {
//                        int newPartInventory = part.getInv() - inventoryChange;
//                        part.setInv(newPartInventory);
//                        partService.save(part);
//                    }
//                    productService.save(product);
//                    entityManager.flush();
//                    System.out.println("Product and parts successfully updated.");
//                    return "confirmationaddproduct";
//                }
//
//            } else if (inventoryChange < 0) {
//
//                System.out.println("Decreasing product inventory by: " + Math.abs(inventoryChange));
//                productService.save(product);
//                entityManager.flush();
//                System.out.println("Product inventory successfully decreased without affecting parts.");
//                return "confirmationaddproduct";
//            } else {
//                // Just save the product
//                System.out.println("No inventory change. Saving product as is.");
//                productService.save(product);
//                return "confirmationaddproduct";
//            }
//        }
//
//        model.addAttribute("error", "Not enough parts.");
//        return "productForm";
//    }
//    @PostMapping("/saveNewProduct")
//    public String saveNewProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model theModel) {
//        if (bindingResult.hasErrors()) {
//            theModel.addAttribute("parts", partService.findAll());
//            List<Part> availParts = new ArrayList<>();
//            for (Part p : partService.findAll()) {
//                if (!product.getParts().contains(p)) availParts.add(p);
//            }
//            theModel.addAttribute("availableParts", availParts);
//            theModel.addAttribute("associatedParts", product.getParts());
//            return "productForm";
//        }
//
//        // Automatically set the dateAdded if it's a new product
//        product.setDateAdded(LocalDateTime.now());
//
//        updateAssociatedParts(product); // Ensure parts are properly associated
//
//        productService.save(product);
//        return "confirmationaddproduct"; // Redirect to a confirmation page
//    }





    private String setupProductForm(Model theModel, Product product) {

        Product existingProduct = productService.findById((int) product.getId());

        theModel.addAttribute("product", existingProduct);


        theModel.addAttribute("associatedParts", existingProduct.getParts());


        List<Part> availableParts = new ArrayList<>();
        for (Part p : partService.findAll()) {
            if (!existingProduct.getParts().contains(p)) {
                availableParts.add(p);
            }
        }
        theModel.addAttribute("availableParts", availableParts);

        return "productForm";
    }




    private void updateAssociatedParts(Product product) {
        for (Part part : product.getParts()) {
            if (!part.getProducts().contains(product)) {
                part.getProducts().add(product);
                partService.save(part);
            }
        }
    }

    @GetMapping("/generateProductReport")
    public void generateProductReport(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"products_report.csv\"");

        List<Product> products = productService.findAll();
        PrintWriter writer = response.getWriter();

        reportService.generateProductReport(products, writer);
    }






}
