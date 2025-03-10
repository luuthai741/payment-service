package com.mupl.payment_service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSuccessPayload {
    private String paymentId;
    private String username;
    private Double amount;
    private LocalDateTime endTime;
}
