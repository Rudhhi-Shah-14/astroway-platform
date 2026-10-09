package com.astroway.auth.scheduler;

import com.astroway.auth.model.User;
import com.astroway.auth.producer.UserEventProducer;
import com.astroway.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GuestCleanupScheduler {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    @Value("${application.security.guest.ttl-hours:24}")
    private long guestTtlHours;

    /**
     * Executes every night at 2:00 AM (Cron: second minute hour day month day-of-week)
     * For testing/local dev, you can use fixedRateString = "3600000" (every hour).
     */
    @Scheduled(fixedRateString = "${application.security.guest.cleanup-rate:3600000}")
    @Transactional
    public void purgeExpiredGuestAccounts() {
        Instant cutoff = Instant.now().minus(guestTtlHours, ChronoUnit.HOURS);
        log.info("Starting automated guest account cleanup for accounts created before {}", cutoff);

        List<User> expiredGuests = userRepository.findExpiredGuests(cutoff);

        if (expiredGuests.isEmpty()) {
            log.info("No expired guest accounts found.");
            return;
        }

        log.info("Found {} expired guest account(s) to purge.", expiredGuests.size());

        for (User guest : expiredGuests) {
            try {
                // 1. Delete user from auth_db (Cascade ALL handles refreshTokens)
                userRepository.delete(guest);

                // 2. Publish event to Kafka for cross-service cleanup in catalog/reviews
                userEventProducer.publishUserDeletedEvent(guest.getId(), guest.getUsername(), guest.getEmail());

                log.info("Purged expired guest user ID: {} ({})", guest.getId(), guest.getUsername());
            } catch (Exception e) {
                log.error("Failed to purge guest user ID: {} due to {}", guest.getId(), e.getMessage(), e);
            }
        }

        log.info("Guest account cleanup completed. Purged {} guest(s).", expiredGuests.size());
    }
}