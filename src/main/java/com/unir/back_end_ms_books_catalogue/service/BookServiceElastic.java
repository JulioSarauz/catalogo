package com.unir.back_end_ms_books_catalogue.service;

import com.unir.back_end_ms_books_catalogue.controller.model.BooksQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.unir.back_end_ms_books_catalogue.data.DataAccessRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceElastic {

    private final DataAccessRepository repository;

    public BooksQueryResponse getBooks(
            String idValue,
            List<String> categoryValues,
            List<String> authorValues,
            List<String> isbnValues,
            List<String> publicationDateValues,
            List<String> ratingValues,
            String title,
            String page) {
        return repository.findBooks(
                idValue,
                categoryValues,
                authorValues,
                isbnValues,
                publicationDateValues,
                ratingValues,
                title,
                page
        );
    }

}