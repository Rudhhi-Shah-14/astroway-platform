package com.astroway.auth.repository;

import com.astroway.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // For Login lookups by username or email
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // For Validation during Registration
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}