package com.astroway.auth.repository;

import com.astroway.auth.model.RefreshToken;
import com.astroway.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    // Revoke all tokens for a specific user (e.g., during logout or password reset)
    void deleteByUser(User user);
}