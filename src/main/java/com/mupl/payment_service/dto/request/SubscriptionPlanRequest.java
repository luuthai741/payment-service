package com.mupl.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanRequest {
    private String subscriptionPlanName;
    private String subscriptionPlanType;
    private Double price;
    private Double discount;
    private Double discountAmount;
    private Integer durationInMonths;
    private Boolean active;
}
