package com.mupl.payment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mupl.payment_service.util.DateUtils;
import com.mupl.payment_service.util.constant.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MomoPaymentResponse extends PaymentCallResponse {
    private String orderId;
    private String requestId;
    private Double amount;
    private String orderType;
    private String transId;
    private String resultCode;
    private String message;
    private String payType;
    private String responseTime;
    private String signature;

    public void setOrderId(String orderId) {
        super.setPaymentId(orderId);
        this.orderId = orderId;
    }

    public void setAmount(Double amount) {
        super.setAmount(amount);
        this.amount = amount;
    }

    public void setPaymentType(String payType) {
        super.setCardType(payType);
        this.payType = payType;
    }

    public void setMessage(String message) {
        super.setResponseMessage(message);
        this.message = message;
    }
    public void setResponseTime(String responseTime) {
        super.setPayDate(LocalDateTime.now());
        this.responseTime = responseTime;
    }
    public void setTransId(String transId) {
        super.setTransactionNo(transId);
        this.transId = transId;
    }
    public void setResultCode(String resultCode) {
        super.setResponseCode(resultCode);
        super.setTransactionStatus(resultCode);
        this.resultCode = resultCode;
    }
    public void setOrderType(String orderType) {
        super.setCardType(orderType);
        super.setPaymentType(PaymentType.MOMO);
        this.orderType = orderType;
    }
}
