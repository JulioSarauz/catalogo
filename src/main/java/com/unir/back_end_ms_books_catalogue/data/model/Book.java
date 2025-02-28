package com.unir.back_end_ms_books_catalogue.data.model;
import com.unir.back_end_ms_books_catalogue.utils.Consts;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Document(indexName = "books", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = Consts.FIELD_TITLE)
    private String title;

    @Field(type = FieldType.Text, name = Consts.FIELD_AUTHOR)
    private String author;

    @Field(type = FieldType.Text, name = Consts.FIELD_ISBN)
    private String isbn;

    @Field(type = FieldType.Date, name = Consts.FIELD_PUBLICATION_DATE)
    private LocalDate publication_date;

    @Field(type = FieldType.Text, format = DateFormat.date, name = Consts.FIELD_RATING)
    private Double rating;

    @Field(type = FieldType.Text, name = Consts.FIELD_PRICE)
    private Integer price;

    @Field(type = FieldType.Text, name = Consts.FIELD_STOCK)
    private Integer stock;

    @Field(type = FieldType.Text, name = Consts.FIELD_IS_VISIBLE)
    private Boolean is_visible;

    @Field(type = FieldType.Keyword, name = Consts.FIELD_CATEGORY_ID)
    private String category_id;

    @Field(type = FieldType.Text, name = Consts.FIELD_IMAGE)
    private String image;

    @Field(type = FieldType.Text, name = Consts.FIELD_SUMMARY)
    private String summary;
}
