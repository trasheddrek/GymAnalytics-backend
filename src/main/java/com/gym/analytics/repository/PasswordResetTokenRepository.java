package com.gym.analytics.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gym.analytics.model.PasswordResetToken;

@Repository 
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    @Query("""
            SELECT prt FROM PasswordResetToken prt
            WHERE prt.token = :token
            """)
    Optional<PasswordResetToken> encontrarPorToken(@Param("token") String token);
}
