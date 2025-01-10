package com.ecommerce.minimart.service;

import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.minimart.domain.Category;
import com.ecommerce.minimart.domain.CategoryRepository;
import com.ecommerce.minimart.domain.Product;
import com.ecommerce.minimart.domain.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository prepository;

    @Autowired
    private CategoryRepository crepository;

    public Iterable<Product> getProducts(String categoryName, String keyword) {
        Category chosenCategory = categoryName != null && !categoryName.isEmpty()
                ? crepository.findByName(categoryName)
                : null;

        String searchedKeyword = keyword != null && !keyword.isEmpty() ? keyword : null;

        if (chosenCategory != null && searchedKeyword != null) {
            return prepository.findByCategoryAndNameContainingIgnoreCase(chosenCategory,
                    searchedKeyword);
        } else if (chosenCategory != null) {
            return prepository.findByCategory(chosenCategory);
        } else if (searchedKeyword != null) {
            return prepository.findByNameContainingIgnoreCase(searchedKeyword);
        } else {
            return prepository.findAll();
        }
    }

    public void createProduct(Product product, MultipartFile imageFile) {
        try {
            product.setImage(Base64.getEncoder().encodeToString(imageFile.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        prepository.save(product);
    }

    public void deleteProductById(Long productId) {
        prepository.deleteById(productId);
    }

    public Product getProductById(Long productId) {
        return prepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));
    }

    public void editProduct(Product product, Long productId, MultipartFile imageFile,
            String deleteProductImage) {
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
    }
}
