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

    @Lock(LockModeType.OPTIMISTIC)
    Optional<AppUser> findAppUserByConfirmationToken(@Param("confirmationToken") String token);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT v FROM AppUser v WHERE v.id = :id")
    Optional<AppUser> findByIdForLock(Long id);
}

