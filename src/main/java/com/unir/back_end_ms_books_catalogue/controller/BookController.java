package com.unir.back_end_ms_books_catalogue.controller;

import com.unir.back_end_ms_books_catalogue.dto.StockUpdateRequest;
import com.unir.back_end_ms_books_catalogue.model.Book;
import com.unir.back_end_ms_books_catalogue.service.BookService;
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
@RequestMapping("/books")
@Tag(name = "Controlador de Libros", description = "Controlador para gestionar los libros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los libros", description = "Recupera una lista de todos los libros disponibles")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un libro por ID", description = "Recupera un libro por su ID")
    @ApiResponse(responseCode = "200", description = "Libro encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    @ApiResponse(responseCode = "404", description = "Libro no encontrado",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo libro", description = "Crea un nuevo libro con los detalles proporcionados")
    @ApiResponse(responseCode = "200", description = "Libro creado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro", description = "Actualiza un libro existente por su ID")
    @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro", description = "Elimina un libro por su ID")
    @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/finds")
    @Operation(summary = "Buscar libros", description = "Busca libros basados en criterios de consulta")
    @ApiResponse(responseCode = "200", description = "Libros recuperados exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Boolean isVisible,
            @RequestParam(required = false) Long categoryId) {
        List<Book> books = bookService.searchBooks(title, stock, author, isbn, rating, isVisible, categoryId);
        return ResponseEntity.ok(books);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un libro", description = "Actualiza parcialmente un libro por su ID")
    @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    @ApiResponse(responseCode = "404", description = "Libro no encontrado",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> patchBook(@PathVariable Long id, @RequestBody String patch) {
        Book patchedBook = bookService.patchBook(id, patch);
        if (patchedBook != null) {
            return ResponseEntity.ok(patchedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/stocks")
    public ResponseEntity<?> updateBookStock(@PathVariable Long id, @RequestBody StockUpdateRequest stockUpdate) {
        try {
            Book updatedBook = bookService.updateBookStock(id, stockUpdate.getStock());
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No hay suficiente stock disponible")) {
                return ResponseEntity.badRequest().body("Error: " + e.getMessage());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
}
