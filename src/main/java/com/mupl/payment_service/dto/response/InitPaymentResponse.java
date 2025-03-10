package com.mupl.payment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mupl.payment_service.util.constant.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitPaymentResponse {
    private String paymentId;
    private PaymentStatus status;
    private Double amount;
    private String paymentUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}
