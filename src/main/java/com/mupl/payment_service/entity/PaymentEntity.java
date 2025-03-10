package com.mupl.payment_service.entity;

import com.mupl.payment_service.util.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mupl_payment")
@Builder
public class PaymentEntity {
    @Id
    private String paymentId;
    private Double amount;
    private String username;
    private Integer subscriptionPlanId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
