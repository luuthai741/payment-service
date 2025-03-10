package com.mupl.payment_service.service;

import com.mupl.payment_service.dto.request.InitPayment;
import com.mupl.payment_service.dto.request.PaymentRequest;
import com.mupl.payment_service.dto.response.InitPaymentResponse;
import com.mupl.payment_service.dto.response.PaymentCallResponse;
import com.mupl.payment_service.dto.response.PaymentProcessResponse;
import com.mupl.payment_service.dto.response.PaymentResponse;
import com.mupl.payment_service.entity.PaymentEntity;
import com.mupl.payment_service.entity.SubscriptionPlanEntity;
import com.mupl.payment_service.repository.PaymentRepository;
import com.mupl.payment_service.repository.SubscriptionPlanRepository;
import com.mupl.payment_service.util.DateUtils;
import com.mupl.payment_service.util.constant.PaymentStatus;
import com.mupl.payment_service.util.constant.PaymentType;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final List<PaymentProcessService> paymentProcessServices;
    private final Map<PaymentType, PaymentProcessService> paymentProcessServiceMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (PaymentProcessService paymentProcessService : paymentProcessServices) {
            paymentProcessServiceMap.put(paymentProcessService.getProviderId(), paymentProcessService);
        }
    }

    public InitPaymentResponse intPayment(PaymentRequest paymentRequest) {
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanRepository.findById(paymentRequest.getPlanId())
                .orElseThrow(() -> new RuntimeException("Subscription plan not found"));
        String suffixPaymentId = RandomStringUtils.random(6, false, true);
        String username = "anonymous";
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .amount(subscriptionPlanEntity.getDiscountAmount())
                .paymentId(DateUtils.convertDateTimeToString(LocalDateTime.now(), DateUtils.yyMMdd + suffixPaymentId))
                .status(PaymentStatus.PENDING)
                .username(username)
                .subscriptionPlanId(subscriptionPlanEntity.getSubscriptionPlanId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        paymentRepository.save(paymentEntity);
        InitPayment initPayment = InitPayment.builder()
                .paymentId(paymentEntity.getPaymentId())
                .amount(paymentEntity.getAmount())
                .username(username)
                .item(subscriptionPlanEntity.getSubscriptionPlanName())
                .planId(subscriptionPlanEntity.getSubscriptionPlanId())
                .build();
        PaymentProcessService paymentProcessService = paymentProcessServiceMap.get(paymentRequest.getPaymentType());
        return paymentProcessService.createPayment(initPayment);
    }

    public PaymentProcessResponse paymentProcess(PaymentCallResponse gatewayResponse) {
        PaymentProcessService paymentProcessService = paymentProcessServiceMap.get(gatewayResponse.getPaymentType());
        return paymentProcessService.paymentProcess(gatewayResponse);
    }

    public PaymentResponse getPaymentById(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId);
    }
}
