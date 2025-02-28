package com.unir.back_end_ms_books_catalogue.controller.model;
import com.unir.back_end_ms_books_catalogue.data.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BooksQueryResponse {
    private List<Book> books;
    private Map<String, List<AggregationDetails>> aggs;
}
