package com.mupl.payment_service.dto.request;

import com.mupl.payment_service.util.constant.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private PaymentType paymentType;
    private Integer planId;
}
