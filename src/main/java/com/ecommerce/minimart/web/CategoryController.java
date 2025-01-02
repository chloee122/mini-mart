package com.ecommerce.minimart.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ecommerce.minimart.domain.Category;
import com.ecommerce.minimart.domain.CategoryRepository;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepository crepository;

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        model.addAttribute("categories", crepository.findAll());
        return "categories";
    }

    @GetMapping("/categories/add")
    public String showCreateCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category_form";
    }

    @PostMapping("/categories/add")
    public String createCategory(Category category) {
        crepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String showEditCategoryForm(@PathVariable("id") Long categoryId, Model model) {
        Category category = crepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));

        model.addAttribute("category", category);
        return "edit_category.html";
    }

    @PostMapping("/categories/edit/{id}")
    public String editCategory(Category category) {
        crepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long categoryId) {
        crepository.deleteById(categoryId);
        return "redirect:/categories";
    }
}
