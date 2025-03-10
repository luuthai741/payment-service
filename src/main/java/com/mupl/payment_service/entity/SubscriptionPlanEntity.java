package com.mupl.payment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mupl_subscription_plan")
public class SubscriptionPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriptionPlanId;
    private String subscriptionPlanName;
    private String subscriptionPlanType;
    private Double price;
    private Double discount;
    private Double discountAmount;
    private Integer durationInMonths;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
