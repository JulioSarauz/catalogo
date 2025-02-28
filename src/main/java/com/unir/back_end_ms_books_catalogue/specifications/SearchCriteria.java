package com.unir.back_end_ms_books_catalogue.specifications;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*; // 🔹 Importa Root, CriteriaQuery, CriteriaBuilder

import java.util.ArrayList;
import java.util.List;

public class SearchCriteria<T> implements Specification<T> {

    private final List<SearchStatement> searchStatements = new ArrayList<>();

    public void add(SearchStatement searchStatement) {
        searchStatements.add(searchStatement);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchStatement param : searchStatements) {
            switch (param.getOperation()) {
                case MATCH:
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(param.getKey())), "%" + param.getValue().toString().toLowerCase() + "%"));
                    break;
                case EQUAL:
                    if (param.getKey().equals("category")) {
                        predicates.add(criteriaBuilder.equal(root.get("category").get("id"), param.getValue()));
                    } else {
                        predicates.add(criteriaBuilder.equal(root.get(param.getKey()), param.getValue()));
                    }
                    break;
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
