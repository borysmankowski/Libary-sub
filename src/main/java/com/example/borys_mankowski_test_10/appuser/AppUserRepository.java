package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<AppUser> findAppUserByConfirmationToken(@Param("confirmationToken") String token);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT v FROM AppUser v WHERE v.id = :id")
    Optional<AppUser> findByIdForLock(Long id);

//    @Query("SELECT u.email " +
//            "FROM Book b " +
//            "JOIN Subscription s ON b.author = s.bookAuthor OR b.category = s.bookCategory " +
//            "JOIN AppUser u ON s.appUser.id = u.id " +
//            "WHERE b.addedDate = CURRENT_DATE " +
//            "GROUP BY u.email")
//    Page<String> findUserEmailsForBooksAddedToday(Pageable pageable);

    @Query("SELECT DISTINCT au FROM AppUser au " +
            "JOIN Subscription s ON s.appUser.id = au.id " +
            "JOIN Book b ON (b.author = s.bookAuthor OR b.category = s.bookCategory) AND b.addedDate = :addedDate " +
            "GROUP BY au.email")
    Page<AppUser> findUsersForBooksAddedToday (LocalDate addedDate, Pageable pageable);
}



