package com.unir.back_end_ms_books_catalogue.specifications;

public enum SearchOperation {
    MATCH,        // LIKE "%value%"
    EQUAL,        // =
    GREATER_THAN, // >
    LESS_THAN     // <
}
