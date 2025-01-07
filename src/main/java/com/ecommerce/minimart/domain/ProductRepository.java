package com.ecommerce.minimart.domain;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Product findByName(String name);

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategoryAndNameContainingIgnoreCase(Category category, String keyword);
}
