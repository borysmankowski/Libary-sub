package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Modifying
    @Query("UPDATE AppUser a SET a.enabled=true WHERE a.email=?1")
    int enableAppUser(String email);

    Optional<AppUser> findBySubscriptionsId(@Param("subscriptionId") Long subscriptionId);

    boolean existsByEmail(String email);

    Optional<AppUser> findAppUserByConfirmationToken(@Param("confirmationToken") String token);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT v FROM AppUser v WHERE v.id = :id")
    Optional<AppUser> findByIdForLock(Long id);
}

