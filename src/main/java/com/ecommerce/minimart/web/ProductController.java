package com.ecommerce.minimart.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ecommerce.minimart.domain.Product;
import com.ecommerce.minimart.service.CategoryService;
import com.ecommerce.minimart.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String getProducts(
            @RequestParam(value = "category", required = false) String categoryName,
            @RequestParam(value = "search", required = false) String keyword, Model model) {
        model.addAttribute("products", productService.getProducts(categoryName, keyword));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("search", keyword);

        return "products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/products/add")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "product_form";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/products/add")
    public String createProduct(@Valid Product product, BindingResult bindingResult, Model model,
            @RequestParam("file") MultipartFile imageFile) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product_form";
        }

        productService.createProduct(product, imageFile);

        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProductById(productId);

        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        model.addAttribute("categories", categoryService.getAllCategories());

        return "edit_product";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Long productId, @Valid Product product,
            BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile imageFile,
            @RequestParam(value = "deleteImage", required = false) String deleteProductImage) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "edit_product";
        }

        productService.editProduct(product, productId, imageFile, deleteProductImage);

        return "redirect:/products";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable("id") Long productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        
        return "product";
    }
}
