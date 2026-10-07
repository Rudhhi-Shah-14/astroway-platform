package com.astroway.auth.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDeletedEvent {

    private String eventId;
    private String eventType; // e.g., "USER_DELETED"
    private Long userId;
    private String username;
    private String email;

    @Builder.Default
    private Instant timestamp = Instant.now();
}