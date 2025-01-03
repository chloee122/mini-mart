package com.ecommerce.minimart.web;

import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepository crepository;

    @Autowired
    private ProductRepository prepository;

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
    public String createCategory(@Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "category_form";
        }
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
    public String editCategory(@Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit_category";
        }
        crepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long categoryId,
            RedirectAttributes redirectAttributes) {
        Category categoryToDelete = crepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));

        List<Product> products = prepository.findByCategory(categoryToDelete);

        if (!products.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete category that has products.");
            return "redirect:/categories";
        }

        crepository.deleteById(categoryId);
        redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        return "redirect:/categories";
    }
}
