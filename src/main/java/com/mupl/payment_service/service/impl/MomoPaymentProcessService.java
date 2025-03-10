package com.mupl.payment_service.service.impl;

import com.mupl.payment_service.dto.request.InitMomoRequest;
import com.mupl.payment_service.dto.request.InitPayment;
import com.mupl.payment_service.dto.response.InitPaymentResponse;
import com.mupl.payment_service.dto.response.PaymentCallResponse;
import com.mupl.payment_service.dto.response.PaymentProcessResponse;
import com.mupl.payment_service.entity.PaymentEntity;
import com.mupl.payment_service.entity.TransactionEntity;
import com.mupl.payment_service.exception.BadRequestException;
import com.mupl.payment_service.feign.MomoClient;
import com.mupl.payment_service.repository.PaymentRepository;
import com.mupl.payment_service.repository.TransactionRepository;
import com.mupl.payment_service.service.PaymentProcessService;
import com.mupl.payment_service.service.UserSubscriptionService;
import com.mupl.payment_service.util.constant.PaymentStatus;
import com.mupl.payment_service.util.constant.PaymentType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MomoPaymentProcessService implements PaymentProcessService {
    @Value("${payment.momo.client}")
    private String clientId;
    @Value("${payment.momo.secret}")
    private String clientSecret;
    @Value("${payment.momo.partner-code}")
    private String partnerCode;
    @Value("${payment.momo.return-url}")
    private String returnUrl;
    private final MomoClient momoClient;
    private final TransactionRepository transactionRepository;
    private final UserSubscriptionService userSubscriptionService;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentType getProviderId() {
        return PaymentType.MOMO;
    }

    @Override
    public InitPaymentResponse createPayment(InitPayment initPayment) {
        return InitPaymentResponse.builder()
                .paymentId(initPayment.getPaymentId())
                .amount(initPayment.getAmount())
                .status(PaymentStatus.PENDING)
                .paymentUrl(getPaymentUrl(initPayment))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String getPaymentUrl(InitPayment initPayment) {
        InitMomoRequest initMomoRequest = InitMomoRequest.builder()
                .orderInfo(initPayment.getItem())
                .amount(initPayment.getAmount().longValue())
                .partnerName("Music Playback")
                .storeId("Music Playback")
                .requestType("captureWallet")
                .redirectUrl(returnUrl)
                .ipnUrl(returnUrl)
                .autoCapture(true)
                .extraData("")
                .partnerCode(partnerCode)
                .orderId(initPayment.getPaymentId())
                .requestId(String.valueOf(System.currentTimeMillis()))
                .startTime(String.valueOf(System.currentTimeMillis()))
                .lang("vi")
                .build();
        initMomoRequest.setSignature(clientId, clientSecret);
        Map<String, String> response = momoClient.initMomoPayment(initMomoRequest);
        return response.get("payUrl");
    }

    @Override
    public PaymentProcessResponse paymentProcess(PaymentCallResponse gatewayResponse) {
        PaymentEntity paymentEntity = paymentRepository.findById(gatewayResponse.getPaymentId())
                .orElseThrow(() -> new BadRequestException("Payment not found"));
        if (StringUtils.equalsAny(paymentEntity.getStatus().toString(), PaymentStatus.SUCCESS.toString(), PaymentStatus.FAILED.toString())) {
            throw new BadRequestException("Payment has already been processed");
        }
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(gatewayResponse.getAmount())
                .bankCode(gatewayResponse.getBankCode())
                .bankTranNo(gatewayResponse.getBankTranNo())
                .cardType(gatewayResponse.getCardType())
                .responseMessage(gatewayResponse.getResponseMessage())
                .responseCode(gatewayResponse.getResponseCode())
                .payDate(gatewayResponse.getPayDate())
                .tmnCode(gatewayResponse.getTmnCode())
                .transactionNo(gatewayResponse.getTransactionNo())
                .transactionStatus(gatewayResponse.getTransactionStatus())
                .payment(paymentEntity)
                .paymentType(gatewayResponse.getPaymentType())
                .build();
        transactionRepository.save(transactionEntity);
        PaymentStatus paymentStatus = getPaymentStatus(gatewayResponse.getTransactionStatus());
        paymentEntity.setStatus(paymentStatus);
        paymentRepository.save(paymentEntity);
        if (paymentStatus.equals(PaymentStatus.SUCCESS)) {
            userSubscriptionService.updateUserSubscription(paymentEntity.getPaymentId());
        }
        return PaymentProcessResponse.builder()
                .paymentId(paymentEntity.getPaymentId())
                .responseCode(gatewayResponse.getResponseCode())
                .responseMessage(gatewayResponse.getResponseMessage())
                .status(paymentStatus)
                .build();
    }

    private PaymentStatus getPaymentStatus(String transactionStatus) {
        if (StringUtils.equalsAny(transactionStatus, "0")) {
            return PaymentStatus.SUCCESS;
        } else {
            return PaymentStatus.FAILED;
        }
    }
}
