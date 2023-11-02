package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.appuser.model.AppUserDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository <AppUser,Long> {

    Optional<AppUser> findByEmail(String email);

    @Modifying
    @Query("UPDATE AppUser a SET a.enabled=true WHERE a.email=?1")
    int enableAppUser(String email);

    Optional<AppUser> findAppUserBySubscriptionsId(@Param("subscriptionId") Long subscriptionId);

    boolean existsByEmail(String email);

    Optional<AppUser> findAppUserByConfirmationToken(@Param("confirmationToken")String token);
}

