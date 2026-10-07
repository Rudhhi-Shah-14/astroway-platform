package com.astroway.auth.controller;

import com.astroway.auth.dto.AuthResponse;
import com.astroway.auth.dto.LoginRequest;
import com.astroway.auth.dto.RegisterRequest;
import com.astroway.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(
            @PathVariable Long id,
            java.security.Principal principal
    ) {
        authService.deleteAccount(id, principal.getName());
        Map<String, String> response = new HashMap<>();
        response.put("message", "User account deleted successfully. Asynchronous cleanup triggered.");
        return ResponseEntity.ok(response);
    }
}