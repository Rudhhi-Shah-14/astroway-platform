package com.astroway.auth.service;

import com.astroway.auth.dto.AuthResponse;
import com.astroway.auth.dto.GoogleAuthRequest;
import com.astroway.auth.dto.LoginRequest;
import com.astroway.auth.dto.RegisterRequest;
import com.astroway.auth.dto.UserSummaryDto;
import com.astroway.auth.model.RefreshToken;
import com.astroway.auth.model.Role;
import com.astroway.auth.model.User;
import com.astroway.auth.producer.UserEventProducer;
import com.astroway.auth.repository.RefreshTokenRepository;
import com.astroway.auth.repository.RoleRepository;
import com.astroway.auth.repository.UserRepository;
import com.astroway.auth.security.GoogleOAuthService;
import com.astroway.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEventProducer userEventProducer;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpirationMs;

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Validate Username and Email Uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // 2. Resolve User Role (Default to ROLE_EXPLORER)
        String rawRole = (request.getRole() != null && !request.getRole().isBlank())
                ? request.getRole().toUpperCase()
                : "ROLE_EXPLORER";

        final String finalRole = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;

        Role userRole = roleRepository.findByName(finalRole)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role specified: " + finalRole));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        // 3. Build & Save User Entity
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // BCrypt Hashing
                .enabled(true)
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);

        // 4. Generate Access & Refresh Tokens
        List<String> roleNames = savedUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String jwtToken = jwtService.generateToken(savedUser.getId(), savedUser.getUsername(), roleNames);
        RefreshToken refreshToken = createRefreshToken(savedUser);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(roleNames)
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        // 1. Authenticate credentials via Spring Security AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        // 2. Load User from Database
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByEmail(request.getUsernameOrEmail())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid username/email or password")));

        // 3. Revoke existing refresh tokens (Token Rotation)
        refreshTokenRepository.deleteByUser(user);

        // 4. Generate new Access and Refresh Tokens
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String jwtToken = jwtService.generateToken(user.getId(), user.getUsername(), roleNames);
        RefreshToken refreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }

    @Transactional
    public AuthResponse loginWithGoogle(String idToken, String requestedRole) {
        // 1. Verify Google Token & Extract Claims
        var payload = googleOAuthService.verifyToken(idToken);
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        // 2. Derive a clean unique username from email/name
        String username = email.split("@")[0].replaceAll("[^a-zA-Z0-9_]", "_");

        // 3. Find existing user or register new OAuth user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    String roleName = (requestedRole != null && !requestedRole.isBlank())
                            ? (requestedRole.startsWith("ROLE_") ? requestedRole : "ROLE_" + requestedRole)
                            : "ROLE_EXPLORER";

                    Role userRole = roleRepository.findByName(roleName)
                            .orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build()));

                    Set<Role> roles = new HashSet<>();
                    roles.add(userRole);

                    return userRepository.save(User.builder()
                            .username(userRepository.existsByUsername(username) ? username + "_" + UUID.randomUUID().toString().substring(0, 4) : username)
                            .email(email)
                            .password(null) // No password for OAuth users
                            .enabled(true)
                            .roles(roles)
                            .build());
                });

        // 4. Revoke previous refresh tokens & issue standard AstroWay JWT
        refreshTokenRepository.deleteByUser(user);

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String jwtToken = jwtService.generateToken(user.getId(), user.getUsername(), roleNames);
        RefreshToken refreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }

    @Transactional
    public AuthResponse loginAsGuest() {
        // 1. Generate unique guest credentials
        String guestUuid = UUID.randomUUID().toString().substring(0, 8);
        String guestUsername = "guest_" + guestUuid;
        String guestEmail = guestUsername + "@guest.astroway.local";

        // 2. Resolve or create ROLE_GUEST
        Role guestRole = roleRepository.findByName("ROLE_GUEST")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_GUEST").build()));

        Set<Role> roles = new HashSet<>();
        roles.add(guestRole);

        // 3. Save Ephemeral Guest User
        User guestUser = User.builder()
                .username(guestUsername)
                .email(guestEmail)
                .password(null) // Guests do not have passwords
                .enabled(true)
                .roles(roles)
                .build();

        User savedGuest = userRepository.save(guestUser);

        // 4. Generate Access & Refresh Tokens
        List<String> roleNames = savedGuest.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String jwtToken = jwtService.generateToken(savedGuest.getId(), savedGuest.getUsername(), roleNames);
        RefreshToken refreshToken = createRefreshToken(savedGuest);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .userId(savedGuest.getId())
                .username(savedGuest.getUsername())
                .email(savedGuest.getEmail())
                .roles(roleNames)
                .build();
    }

    @Transactional
    public void deleteAccount(Long userId, String authenticatedUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Ensure user can only delete their own account (unless ADMIN)
        if (!user.getUsername().equals(authenticatedUsername)) {
            throw new IllegalArgumentException("Unauthorized to delete this account");
        }

        // 1. Revoke Refresh Tokens & Delete User from auth_db
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);

        // 2. Publish Async Event to Kafka for downstream catalog-service cleanup
        userEventProducer.publishUserDeletedEvent(user.getId(), user.getUsername(), user.getEmail());
    }

    @Transactional(readOnly = true)
    public Page<UserSummaryDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        return userRepository.findAll(pageable)
                .map(user -> UserSummaryDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .enabled(user.isEnabled())
                        .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .createdAt(user.getCreatedAt())
                        .build()
                );
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}