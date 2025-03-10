package com.mupl.payment_service.service;

import com.mupl.payment_service.dto.request.InitPayment;
import com.mupl.payment_service.dto.response.InitPaymentResponse;
import com.mupl.payment_service.dto.response.PaymentCallResponse;
import com.mupl.payment_service.dto.response.PaymentProcessResponse;
import com.mupl.payment_service.util.constant.PaymentType;

public interface PaymentProcessService {
    PaymentType getProviderId();
    InitPaymentResponse createPayment(InitPayment initPayment);
    PaymentProcessResponse paymentProcess(PaymentCallResponse paymentCallResponse);
}
