package com.example.borys_mankowski_test_10.subscription;

import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    boolean existsByAppUserIdAndBookCategory(Long appUserId, String category);

    @Lock(LockModeType.OPTIMISTIC)
    boolean existsByAppUserIdAndBookAuthor(Long appUserId, String author);

    Page<Subscription> findAllByAppUser_Email(String appUserEmail, Pageable pageable);
}
