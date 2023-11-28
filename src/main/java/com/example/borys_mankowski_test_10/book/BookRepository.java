package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.addedDate = CURRENT_DATE")
    List<Book> findAllByAddedDateToday();

    @Query("SELECT b FROM Book b WHERE b.addedDate = CURRENT_DATE")
    List<Book> findBookByAuthorAddedToday(String author);

    @Query("SELECT b FROM Book b WHERE b.addedDate = CURRENT_DATE")
    List<Book> findBookByCategoryAddedToday(String category);

}
