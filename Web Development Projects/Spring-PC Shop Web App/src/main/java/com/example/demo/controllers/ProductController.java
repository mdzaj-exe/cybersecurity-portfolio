package com.example.demo.controllers;

import com.example.demo.domain.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    private static Map<Integer, Integer> cart = new HashMap<>(); // Cart with product ID and quantity

    @GetMapping("/buyProduct")
    public String buyProduct(@RequestParam("productID") int productId, @RequestParam(name = "quantity", defaultValue = "1") int quantity, RedirectAttributes redirectAttributes) {
        Product product = productService.findById(productId);
        String message;

        if (product != null) {
            if (product.getInv() >= quantity) {
                product.setInv(product.getInv() - quantity);
                productService.save(product);
                cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
                message = "Purchase successful! The inventory for " + product.getName() + " has been decremented.";
            } else {
                message = "Purchase failed. The inventory for " + product.getName() + " is insufficient.";
            }
        } else {
            message = "Product not found.";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/viewCart"; // Redirect to view cart after purchase
    }

    @GetMapping("/removeFromCart")
    public String removeFromCart(@RequestParam("productID") int productId, RedirectAttributes redirectAttributes) {
        Product product = productService.findById(productId);
        int quantity = cart.getOrDefault(productId, 0);

        if (product != null && quantity > 0) {
            product.setInv(product.getInv() + quantity); // Re-increment inventory
            productService.save(product);
            cart.remove(productId);
        }

        redirectAttributes.addFlashAttribute("message", "Item removed from cart.");
        return "redirect:/viewCart";
    }

    @GetMapping("/viewCart")
    public String viewCart(Model theModel) {
        // Fetch product details to display in the cart
        Map<Integer, Product> productsInCart = new HashMap<>();
        double total = 0;
        for (Integer id : cart.keySet()) {
            Product product = productService.findById(id);
            if (product != null) {
                productsInCart.put(id, product);
                total += product.getPrice() * cart.get(id);
            }
        }
        theModel.addAttribute("cart", cart);
        theModel.addAttribute("productsInCart", productsInCart);
        theModel.addAttribute("total", total); // Add total to the model
        return "productList";
    }
}
