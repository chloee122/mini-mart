package com.ecommerce.minimart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.minimart.domain.Category;
import com.ecommerce.minimart.domain.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository crepository;

    public Iterable<Category> getAllCategories() {
        return crepository.findAll();
    }
}
