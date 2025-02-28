package com.unir.back_end_ms_books_catalogue.data;

import com.unir.back_end_ms_books_catalogue.controller.model.AggregationDetails;
import com.unir.back_end_ms_books_catalogue.data.model.Book;
import com.unir.back_end_ms_books_catalogue.utils.Consts;
import com.unir.back_end_ms_books_catalogue.controller.model.BooksQueryResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {

    private final BookRepositoryElastic bookRepositoryElastic;
    private final ElasticsearchOperations elasticClient;

    @SneakyThrows
    public BooksQueryResponse findBooks(
            List<String> categoryValues,
            List<String> authorValues,
            List<String> isbnValues,
            List<String> publicationDateValues,
            List<String> ratingValues,
            String title,
            String page) {

        try {
            BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

            // Filtrar por autor
            if (authorValues != null && !authorValues.isEmpty()) {
                authorValues.forEach(
                        author -> querySpec.must(QueryBuilders.termQuery("author.keyword", author))
                );
            }

            // Filtrar por categoría
            if (categoryValues != null && !categoryValues.isEmpty()) {
                categoryValues.forEach(
                        category -> querySpec.must(QueryBuilders.termQuery("category.keyword", category))
                );
            }

            // Filtrar por ISBN
            if (isbnValues != null && !isbnValues.isEmpty()) {
                isbnValues.forEach(
                        isbn -> querySpec.must(QueryBuilders.termQuery("isbn.keyword", isbn))
                );
            }

            // Filtrar por fecha de publicación
            if (publicationDateValues != null && !publicationDateValues.isEmpty()) {
                publicationDateValues.forEach(
                        dateRange -> {
                            String[] range = dateRange.split("-");
                            if (range.length == 2) {
                                querySpec.must(QueryBuilders.rangeQuery("publication_date")
                                        .from(range[0])
                                        .to(range[1])
                                        .includeUpper(false));
                            } else if (range.length == 1) {
                                querySpec.must(QueryBuilders.rangeQuery("publication_date")
                                        .from(range[0]));
                            }
                        }
                );
            }

            // Filtrar por rating
            if (ratingValues != null && !ratingValues.isEmpty()) {
                ratingValues.forEach(
                        ratingRange -> {
                            String[] range = ratingRange.split("-");
                            if (range.length == 2) {
                                querySpec.must(QueryBuilders.rangeQuery("rating")
                                        .from(range[0])
                                        .to(range[1])
                                        .includeUpper(false));
                            } else if (range.length == 1) {
                                querySpec.must(QueryBuilders.rangeQuery("rating")
                                        .from(range[0]));
                            }
                        }
                );
            }


// Filtro si no se aplica ninguno de los anteriores
            if (!querySpec.hasClauses()) {
                querySpec.must(QueryBuilders.matchAllQuery());
            }

// Construcción de la consulta
            NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

// Agregaciones
            searchQueryBuilder.addAggregation(AggregationBuilders.terms("agg_author").field("author.keyword").size(10));

// Paginación
            int pageInt = (page != null && !page.isEmpty()) ? Integer.parseInt(page) : 0;
            searchQueryBuilder.withPageable(PageRequest.of(pageInt, 5));

// Ejecutar la consulta
            Query query = searchQueryBuilder.build();

// Imprimir la consulta en formato JSON
            String queryJson = query.toString();
            log.info("Consulta generada: {}", queryJson);

// Ejecutar la consulta en Elasticsearch
            SearchHits<Book> result = elasticClient.search(query, Book.class);

// Obtener los libros y agregaciones
            List<Book> books = getResponseBooks(result);
            Map<String, List<AggregationDetails>> aggregations = getResponseAggregations(result);

// Devuelve la respuesta con los libros y las agregaciones
            return new BooksQueryResponse(books, aggregations);

        } catch (Exception e) {
            log.error("Error al ejecutar la consulta", e);
        }

        return null;
    }

    private List<Book> getResponseBooks(SearchHits<Book> result) {
        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

    private Map<String, List<AggregationDetails>> getResponseAggregations(SearchHits<Book> result) {
        Map<String, List<AggregationDetails>> responseAggregations = new HashMap<>();

        if (result.hasAggregations()) {
            Map<String, Aggregation> aggs = result.getAggregations().asMap();

            aggs.forEach((key, value) -> {
                responseAggregations.putIfAbsent(key, new LinkedList<>());

                if (value instanceof ParsedStringTerms parsedStringTerms) {
                    parsedStringTerms.getBuckets().forEach(bucket ->
                            responseAggregations.get(key).add(new AggregationDetails(bucket.getKeyAsString(), (int) bucket.getDocCount())));
                }

                if (value instanceof ParsedRange parsedRange) {
                    parsedRange.getBuckets().forEach(bucket ->
                            responseAggregations.get(key).add(new AggregationDetails(bucket.getKeyAsString(), (int) bucket.getDocCount())));
                }
            });
        }
        return responseAggregations;
    }
}
