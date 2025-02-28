package com.unir.back_end_ms_books_catalogue.repository;

import com.unir.back_end_ms_books_catalogue.model.Category;
import com.unir.back_end_ms_books_catalogue.specifications.SearchCriteria;
import com.unir.back_end_ms_books_catalogue.specifications.SearchOperation;
import com.unir.back_end_ms_books_catalogue.specifications.SearchStatement;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryRepository(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    // 🔹 Buscar categorías por nombre (búsqueda dinámica)
    public List<Category> searchCategories(String name) {
        SearchCriteria<Category> spec = new SearchCriteria<>();

        if (StringUtils.hasLength(name)) {
            spec.add(new SearchStatement("name", name, SearchOperation.MATCH));
        }

        return categoryJpaRepository.findAll(spec);
    }


}