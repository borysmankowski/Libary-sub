package com.example.borys_mankowski_test_10.book.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "books",
        indexes = {
                @Index(name = "id_index", columnList = "id"),
                @Index(name = "author_index", columnList = "author"),
                @Index(name = "title_author", columnList = "title"),
                @Index(name = "added_date_index", columnList = "added_date"),
                @Index(name = "category_index", columnList = "category"),
        }
)

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    @Column(unique = true)
    private String title;

    private String category;

    @Column(name = "added_date")
    private LocalDate addedDate;

    @Version
    private Integer version;
}
