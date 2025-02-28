package com.unir.back_end_ms_books_catalogue.controller;

import com.unir.back_end_ms_books_catalogue.model.Category;
import com.unir.back_end_ms_books_catalogue.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Tag(name = "Controlador de Categorías", description = "Controlador para gestionar las categorías")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Obtiene una lista de todas las categorías disponibles")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por ID", description = "Obtiene los detalles de una categoría específica por su ID")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría con los datos proporcionados")
    @ApiResponse(responseCode = "201", description = "Categoría creada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría", description = "Actualiza una categoría existente usando su ID")
    @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría usando su ID")
    @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/finds")
    @Operation(summary = "Buscar categorías", description = "Busca categorías basadas en criterios de consulta")
    @ApiResponse(responseCode = "200", description = "Resultados de la búsqueda devueltos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    public ResponseEntity<List<Category>> searchCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId) {
        List<Category> categories = categoryService.searchCategories(name);
        return ResponseEntity.ok(categories);
    }
}
