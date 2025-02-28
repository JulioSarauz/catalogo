package com.unir.back_end_ms_books_catalogue.repository;

import com.unir.back_end_ms_books_catalogue.model.Book;
import com.unir.back_end_ms_books_catalogue.specifications.SearchCriteria;
import com.unir.back_end_ms_books_catalogue.specifications.SearchOperation;
import com.unir.back_end_ms_books_catalogue.specifications.SearchStatement;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class BookRepository {

    private final BookJpaRepository bookJpaRepository;

    public BookRepository(BookJpaRepository bookJpaRepository) {
        this.bookJpaRepository = bookJpaRepository;
    }

    public List<Book> searchBooks(String title, String author, String isbn, Double rating, Boolean isVisible, Long categoryId) {
        SearchCriteria<Book> spec = new SearchCriteria<>();

        if (StringUtils.hasLength(title)) {
            spec.add(new SearchStatement("title", title, SearchOperation.MATCH));
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
            spec.add(new SearchStatement("category.id", categoryId, SearchOperation.EQUAL));
        }

        return bookJpaRepository.findAll(spec);
    }
}
