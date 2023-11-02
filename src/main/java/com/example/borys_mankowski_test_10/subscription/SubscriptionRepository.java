package com.example.borys_mankowski_test_10.subscription;

import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    boolean existsByAppUserIdAndBookCategory(Long appUserId, String category);

    @Lock(LockModeType.OPTIMISTIC)
    boolean existsByAppUserIdAndBookAuthor(Long appUserId, String author);

    @Query("SELECT s FROM Subscription s WHERE s.bookAuthor = :author OR s.bookCategory = :category")
    List<Subscription> findSubscriptionsByAuthorOrCategory(String author, String category, Pageable pageable);

}
