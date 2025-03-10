package com.mupl.payment_service.dto.response;

import com.mupl.payment_service.util.constant.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCallResponse {
    protected String paymentId;
    protected Double amount;
    protected String bankCode;
    protected String bankTranNo;
    protected String cardType;
    protected String responseMessage;
    protected LocalDateTime payDate;
    protected String responseCode;
    protected String tmnCode;
    protected String transactionNo;
    protected String transactionStatus;
    protected PaymentType paymentType;
}
