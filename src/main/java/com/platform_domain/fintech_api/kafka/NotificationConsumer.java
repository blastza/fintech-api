package com.platform_domain.fintech_api.kafka;

import com.platform_domain.fintech_api.event.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = "transactions", groupId = "notification-group")
    public void handleTransaction(TransactionEvent event) {
        String message = buildMessage(event);
        // In a real system: send an email or SMS here via SendGrid, Twilio, etc.
        log.info("[NOTIFICATION] Sending to {}: {}", event.getUserEmail(), message);
    }

    private String buildMessage(TransactionEvent event) {
        return switch (event.getType()) {
            case "DEPOSIT" -> String.format("R%.2f has been deposited into your account.",
                    event.getAmount());
            case "WITHDRAWAL" -> String.format("R%.2f has been withdrawn from your account.",
                    event.getAmount());
            case "TRANSFER_OUT" -> String.format("R%.2f has been transferred out of your account.",
                    event.getAmount());
            case "TRANSFER_IN" -> String.format("R%.2f has been received in your account.",
                    event.getAmount());
            default -> "A transaction has occurred on your account.";
        };
    }
}