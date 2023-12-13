package com.example.borys_mankowski_test_10.book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "books",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title"})
        },
        indexes = {
                @Index(name = "added_date_index", columnList = "added_date"),
                @Index(name = "category_index", columnList = "category"),
                @Index(name = "author_index", columnList = "author")
        }
)

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    private String title;

    private String category;

    @Column(name = "added_date")
    private LocalDate addedDate;

    @Version
    private Integer version;
}
