package com.platform_domain.fintech_api.kafka;

import com.platform_domain.fintech_api.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class FraudConsumer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    // Threshold for suspicious transactions (R50,000)
    private static final BigDecimal SUSPICIOUS_AMOUNT = new BigDecimal("50000");

    @KafkaListener(topics = "transactions", groupId = "fraud-group")
    public void handleTransaction(TransactionEvent event) {
        if (isSuspicious(event)) {
            log.warn("[FRAUD ALERT] Suspicious transaction detected: type={} amount={} account={}",
                    event.getType(), event.getAmount(), event.getAccountId());
            // Publish to a separate fraud-alerts topic — another team's system reads this
            kafkaTemplate.send("fraud-alerts", event.getAccountId().toString(), event);
        }
    }

    private boolean isSuspicious(TransactionEvent event) {
        // Rule 1: Large amount
        if (event.getAmount().compareTo(SUSPICIOUS_AMOUNT) > 0) return true;

        // Rule 2: You can add more rules here as you learn
        // e.g. multiple transactions in short time, unusual hours, etc.

        return false;
    }
}
