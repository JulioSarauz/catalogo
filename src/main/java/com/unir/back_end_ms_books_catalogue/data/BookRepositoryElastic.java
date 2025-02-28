package com.unir.back_end_ms_books_catalogue.data;

import java.util.List;
import com.unir.back_end_ms_books_catalogue.data.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepositoryElastic extends ElasticsearchRepository<Book, String> {
    List<Book> findAll();
}