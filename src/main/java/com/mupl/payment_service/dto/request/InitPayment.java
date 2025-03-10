package com.mupl.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitPayment {
    private String paymentId;
    private Double amount;
    private String username;
    private String item;
    private Integer planId;
}
