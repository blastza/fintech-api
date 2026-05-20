package com.platform_domain.fintech_api.kafka;

import com.platform_domain.fintech_api.event.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditConsumer {

    @KafkaListener(topics = "transactions", groupId = "audit-group")
    public void handleTransaction(TransactionEvent event) {
        // In a real system this writes to an immutable audit log table
        // For now, structured logging is fine
        log.info("[AUDIT] type={} amount={} account={} counterpart={} user={} time={}",
                event.getType(),
                event.getAmount(),
                event.getAccountId(),
                event.getCounterpartAccountId(),
                event.getUserEmail(),
                event.getOccurredAt()
        );
    }
}