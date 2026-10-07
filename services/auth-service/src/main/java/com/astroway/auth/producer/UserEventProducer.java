package com.astroway.auth.producer;

import com.astroway.auth.event.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, UserDeletedEvent> kafkaTemplate;

    @Value("${application.kafka.topics.user-deleted}")
    private String userDeletedTopic;

    public void publishUserDeletedEvent(Long userId, String username, String email) {
        UserDeletedEvent event = UserDeletedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("USER_DELETED")
                .userId(userId)
                .username(username)
                .email(email)
                .build();

        log.info("Publishing UserDeletedEvent for userId: {} to topic: {}", userId, userDeletedTopic);

        CompletableFuture<SendResult<String, UserDeletedEvent>> future =
                kafkaTemplate.send(userDeletedTopic, userId.toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully published UserDeletedEvent for userId: {} [Partition: {}, Offset: {}]",
                        userId,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish UserDeletedEvent for userId: {} due to: {}", userId, ex.getMessage(), ex);
            }
        });
    }
}