package com.platform_domain.fintech_api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {

    private Long transactionId;
    private Long accountId;
    private Long counterpartAccountId;  // null if not a transfer
    private String type;                // DEPOSITE, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    private BigDecimal amount;
    private String description;
    private String userEmail;
    private LocalDateTime occurredAt;
}
