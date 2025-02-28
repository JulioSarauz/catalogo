package com.unir.back_end_ms_books_catalogue.controller;
import com.unir.back_end_ms_books_catalogue.controller.model.BooksQueryResponse;
import com.unir.back_end_ms_books_catalogue.service.BookServiceElastic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class BookControllerElastic {
    private final BookServiceElastic service;

    @GetMapping("/publications")
    public ResponseEntity<BooksQueryResponse> getBooks(
            @RequestParam(required = false) String idValue,
            @RequestParam(required = false) List<String> categoryValues,
            @RequestParam(required = false) List<String> authorValues,
            @RequestParam(required = false) List<String> isbnValues,
            @RequestParam(required = false) List<String> publicationDateValues,
            @RequestParam(required = false) List<String> ratingValues,
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "0") String page) {

        BooksQueryResponse response = service.getBooks(
                idValue,
                categoryValues,
                authorValues,
                isbnValues,
                publicationDateValues,
                ratingValues,
                title,
                page);
        return ResponseEntity.ok(response);
    }
}
