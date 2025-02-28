package com.unir.back_end_ms_books_catalogue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.back_end_ms_books_catalogue.model.Book;
import com.unir.back_end_ms_books_catalogue.model.Category;
import com.unir.back_end_ms_books_catalogue.repository.BookJpaRepository;
import com.unir.back_end_ms_books_catalogue.repository.CategoryJpaRepository;
import com.unir.back_end_ms_books_catalogue.repository.CategoryRepository;
import com.unir.back_end_ms_books_catalogue.specifications.SearchCriteria;
import com.unir.back_end_ms_books_catalogue.specifications.SearchOperation;
import com.unir.back_end_ms_books_catalogue.specifications.SearchStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookJpaRepository bookRepository;
    private final CategoryJpaRepository categoryRepository;
    private final ObjectMapper objectMapper;



    public BookService(BookJpaRepository bookRepository, CategoryJpaRepository categoryRepository, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    // 🔹 Obtener todos los libros
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // 🔹 Obtener un libro por ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // 🔹 Crear un nuevo libro
    public Book createBook(Book book) {
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Category category = categoryRepository.findById(book.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            book.setCategory(category);
        }
        return bookRepository.save(book);
    }

    // 🔹 Actualizar un libro
    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setIsbn(bookDetails.getIsbn());
            book.setPublicationDate(bookDetails.getPublicationDate());
            book.setRating(bookDetails.getRating());
            book.setIsVisible(bookDetails.getIsVisible());
            book.setPrice(bookDetails.getPrice());
            book.setStock(bookDetails.getStock());

            // 🔹 Verificar si la categoría cambió y buscarla en la BD
            if (bookDetails.getCategory() != null && bookDetails.getCategory().getId() != null) {
                Category category = categoryRepository.findById(bookDetails.getCategory().getId())
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                book.setCategory(category);
            }

            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }


    public Book patchBook(Long id, String request) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            try {
                JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
                JsonNode bookNode = objectMapper.valueToTree(book);
                JsonNode patchedNode = jsonMergePatch.apply(bookNode);
                Book patched = objectMapper.treeToValue(patchedNode, Book.class);
                return bookRepository.save(patched);
            } catch (JsonProcessingException | JsonPatchException e) {
                System.err.println("Error processing JSON patch: " + e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    // 🔹 Eliminar un libro por ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // 🔹 Buscar libros con filtros dinámicos
    public List<Book> searchBooks(String title, Integer stock, String author, String isbn, Double rating, Boolean isVisible, Long categoryId) {
        SearchCriteria<Book> spec = new SearchCriteria<>();

        if (StringUtils.hasLength(title)) {
            spec.add(new SearchStatement("title", title, SearchOperation.MATCH));
        }
        if (stock != null) {
            spec.add(new SearchStatement("stock", stock, SearchOperation.EQUAL));
        }
        if (StringUtils.hasLength(author)) {
            spec.add(new SearchStatement("author", author, SearchOperation.MATCH));
        }
        if (StringUtils.hasLength(isbn)) {
            spec.add(new SearchStatement("isbn", isbn, SearchOperation.EQUAL));
        }
        if (rating != null) {
            spec.add(new SearchStatement("rating", rating, SearchOperation.EQUAL));
        }
        if (isVisible != null) {
            spec.add(new SearchStatement("isVisible", isVisible, SearchOperation.EQUAL));
        }
        if (categoryId != null) {

            spec.add(new SearchStatement("category", categoryId, SearchOperation.EQUAL));

        }

        return bookRepository.findAll(spec);
    }

    public Book updateBookStock(Long id, Integer stockToDeduct) {
        return bookRepository.findById(id).map(book -> {
            if (book.getStock() != null && book.getStock() >= stockToDeduct) {
                book.setStock(book.getStock() - stockToDeduct);
                return bookRepository.save(book);
            } else {
                throw new RuntimeException("No hay suficiente stock disponible. Stock actual: " + book.getStock());
            }
        }).orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id));
    }
}
