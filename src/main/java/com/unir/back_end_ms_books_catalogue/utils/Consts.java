package com.unir.back_end_ms_books_catalogue.utils;

public class Consts {  private Consts() {
    throw new IllegalStateException("Utility class");
}

    //Nombres de campos
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_AUTHOR = "author";
    public static final String FIELD_ISBN = "isbn";
    public static final String FIELD_PUBLICATION_DATE = "publication_date";
    public static final String FIELD_RATING = "rating";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_STOCK = "stock";
    public static final String FIELD_IS_VISIBLE = "is_visible";
    public static final String FIELD_CATEGORY_ID = "category_id";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_SUMMARY = "summary";
    //Nombres de agregaciones
    public static final String AGG_KEY_TERM_CATEGORY = "categoryValues";
    public static final String AGG_KEY_TERM_AUTHOR = "authorValues";
    public static final String AGG_KEY_TERM_ISBN = "isbnValues";
    public static final String AGG_KEY_RANGE_PUBLICATION_DATE = "publicationDateValues";
    public static final String AGG_KEY_RANGE_RATING = "ratingValues";



}
