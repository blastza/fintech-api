package com.platform_domain.fintech_api.kafka;

import com.platform_domain.fintech_api.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public void publishTransactionEvent(TransactionEvent event) {
        kafkaTemplate.send("transactions", event.getAccountId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish transaction event: {}", ex.getMessage());
                    } else {
                        log.info("Publish transaction event: type={} amount={} account={}",
                                event.getType(), event.getAmount(), event.getAccountId());
                    }
                });
    }
}
