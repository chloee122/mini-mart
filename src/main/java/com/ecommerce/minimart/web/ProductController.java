package com.ecommerce.minimart.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ecommerce.minimart.domain.Product;
import com.ecommerce.minimart.domain.ProductRepository;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProductController {
    @Autowired
    private ProductRepository prepository;

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", prepository.findAll());
        return "products";
    }

    @GetMapping("/products/add")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    @PostMapping("/products/add")
    public String createProduct(Product product) {
        prepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        prepository.deleteById(productId);
        return "redirect:../products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long productId, Model model) {
        Product product = prepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));

        model.addAttribute("product", product);
        return "edit_product";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(Product product) {
        prepository.save(product);
        return "redirect:/products";
    }
}
