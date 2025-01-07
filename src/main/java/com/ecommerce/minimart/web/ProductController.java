package com.ecommerce.minimart.web;

import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ecommerce.minimart.domain.Category;
import com.ecommerce.minimart.domain.CategoryRepository;
import com.ecommerce.minimart.domain.Product;
import com.ecommerce.minimart.domain.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ProductController {
    @Autowired
    private ProductRepository prepository;

    @Autowired
    private CategoryRepository crepository;

    @GetMapping("/products")
    public String getProducts(
            @RequestParam(value = "category", required = false) String categoryName,
            @RequestParam(value = "search", required = false) String keyword, Model model) {

        Iterable<Product> products;

        Category chosenCategory = categoryName != null && !categoryName.isEmpty()
                ? crepository.findByName(categoryName)
                : null;

        String searchedKeyword = keyword != null && !keyword.isEmpty() ? keyword : null;

        if (chosenCategory != null && searchedKeyword != null) {
            products = prepository.findByCategoryAndNameContainingIgnoreCase(chosenCategory,
                    searchedKeyword);
        } else if (chosenCategory != null) {
            products = prepository.findByCategory(chosenCategory);
        } else if (searchedKeyword != null) {
            products = prepository.findByNameContainingIgnoreCase(searchedKeyword);
        } else {
            products = prepository.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", crepository.findAll());
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("search", searchedKeyword);

        return "products";
    }

    @GetMapping("/products/add")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", crepository.findAll());
        return "product_form";
    }

    @PostMapping("/products/add")
    public String createProduct(@Valid Product product, BindingResult bindingResult, Model model,
            @RequestParam("file") MultipartFile imageFile) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", crepository.findAll());
            return "product_form";
        }

        try {
            product.setImage(Base64.getEncoder().encodeToString(imageFile.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        prepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        prepository.deleteById(productId);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long productId, Model model) {
        Product product = prepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));

        model.addAttribute("product", product);
        model.addAttribute("categories", crepository.findAll());
        return "edit_product";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Long productId, @Valid Product product,
            BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile imageFile,
            @RequestParam(value = "deleteImage", required = false) String deleteProductImage) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", crepository.findAll());
            return "edit_product";
        }

        if (!imageFile.isEmpty()) {
            try {
                product.setImage(Base64.getEncoder().encodeToString(imageFile.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("true".equals(deleteProductImage)) {
            product.setImage(null);
        } else {
            Product existingProduct = prepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));
            product.setImage(existingProduct.getImage());
        }

        prepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}")
    public String getAProduct(@PathVariable("id") Long productId, Model model) {
        Product product = prepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));

        model.addAttribute("product", product);
        return "product";
    }
}
