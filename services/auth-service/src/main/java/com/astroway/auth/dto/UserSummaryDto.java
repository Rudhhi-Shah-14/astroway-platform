package com.astroway.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDto {

    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private List<String> roles;
    private Instant createdAt;
}