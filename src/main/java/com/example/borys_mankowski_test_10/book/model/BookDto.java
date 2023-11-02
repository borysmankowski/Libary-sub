package com.example.borys_mankowski_test_10.book.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String category;
    private boolean available;
}
