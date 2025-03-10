package com.mupl.payment_service.dto.response;

import com.mupl.payment_service.util.constant.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProcessResponse {
    private PaymentStatus status;
    private String paymentId;
    private String responseMessage;
    private String responseCode;
}
