package com.unir.back_end_ms_books_catalogue.service;

import com.unir.back_end_ms_books_catalogue.model.Category;
import com.unir.back_end_ms_books_catalogue.repository.CategoryJpaRepository;
import com.unir.back_end_ms_books_catalogue.specifications.SearchCriteria;
import com.unir.back_end_ms_books_catalogue.specifications.SearchOperation;
import com.unir.back_end_ms_books_catalogue.specifications.SearchStatement;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryJpaRepository categoryRepository;

    public CategoryService(CategoryJpaRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 🔹 Obtener todas las categorías
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 🔹 Obtener una categoría por ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // 🔹 Crear una nueva categoría
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // 🔹 Actualizar una categoría
    public Category updateCategory(Long id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDetails.getName());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    // 🔹 Eliminar una categoría por ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // 🔹 Buscar categorías con filtros dinámicos
    public List<Category> searchCategories(String name) {
        SearchCriteria<Category> spec = new SearchCriteria<>();

        if (StringUtils.hasLength(name)) {
            spec.add(new SearchStatement("name", name, SearchOperation.MATCH));
        }

        return categoryRepository.findAll(spec);
    }
}