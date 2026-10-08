package com.astroway.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleAuthRequest {

    @NotBlank(message = "Google ID token is required")
    private String idToken;

    // Optional role override if registering via Google for the first time
    private String role;
}