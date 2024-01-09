package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT DISTINCT b FROM Book b WHERE b.addedDate = :addedDate " +
            "AND (b.category = :category OR b.author = :author)")
    Page<Book> findAllByAddedDateAndCategoryOrAuthor(
            LocalDate addedDate,
            String category,
            String author,
            Pageable pageable);

}
