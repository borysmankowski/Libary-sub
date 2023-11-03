package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByTitle(String title);
    @Query("SELECT b FROM Book b WHERE b.addedDate = CURRENT_DATE")
    List<Book> findAllByAddedDateToday();


}
