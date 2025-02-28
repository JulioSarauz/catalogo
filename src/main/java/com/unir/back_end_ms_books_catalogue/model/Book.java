package com.unir.back_end_ms_books_catalogue.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String author;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    private Double rating;

    private Double price;

    @Column(nullable = false)
    private Integer stock; // Cantidad de libros en stock

    @Column(nullable = false)
    private Boolean isVisible; // Indica si el libro está disponible o no

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
