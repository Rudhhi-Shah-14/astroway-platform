package com.astroway.auth.repository;

import com.astroway.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // For Login lookups by username or email
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // For Validation during Registration
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Fetch guest users older than specified cutoff timestamp
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_GUEST' AND u.createdAt < :cutoff")
    List<User> findExpiredGuests(@Param("cutoff") Instant cutoff);
}