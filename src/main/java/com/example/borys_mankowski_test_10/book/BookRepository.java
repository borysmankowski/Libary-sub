package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT DISTINCT b FROM AppUser au " +
            "JOIN Subscription s ON s.appUser.id = au.id " +
            "JOIN Book b ON (b.author = s.bookAuthor OR b.category = s.bookCategory) AND b.addedDate = current_date()" +
            "AND au.id = :appUserId")
    Page<Book> findAllAddedTodayMatchingUserSubscriptions(
            Long appUserId,
            Pageable pageable);

}
