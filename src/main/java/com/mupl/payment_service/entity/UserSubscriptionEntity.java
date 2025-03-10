package com.mupl.payment_service.entity;

import com.mupl.payment_service.util.constant.UserSubscriptionStatus;
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
@Table(name = "mupl_user_subscription")
@Builder
public class UserSubscriptionEntity {
    @Id
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private UserSubscriptionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
