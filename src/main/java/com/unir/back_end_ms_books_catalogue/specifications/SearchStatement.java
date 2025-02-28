package com.unir.back_end_ms_books_catalogue.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchStatement {
    private String key;
    private Object value;
    private SearchOperation operation;
}
