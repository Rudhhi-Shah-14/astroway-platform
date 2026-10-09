package com.astroway.catalog.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final JdbcTemplate jdbcTemplate;

    @KafkaListener(topics = "${application.kafka.topics.user-deleted}", groupId = "catalog-service-group")
    @Transactional
    public void handleUserDeletedEvent(Map<String, Object> event) {
        Object userIdObj = event.get("userId");
        if (userIdObj != null) {
            Long userId = Long.valueOf(userIdObj.toString());
            log.info("Consuming UserDeletedEvent for userId {}. Purging favorites from catalog_db...", userId);

            int deletedRows = jdbcTemplate.update("DELETE FROM user_favorite_spots WHERE user_id = ?", userId);
            log.info("Purged {} favorite records for user ID {}", deletedRows, userId);
        }
    }
}