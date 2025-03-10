package com.mupl.payment_service.controller;

import com.mupl.payment_service.dto.request.PaymentRequest;
import com.mupl.payment_service.dto.response.InitPaymentResponse;
import com.mupl.payment_service.dto.response.MomoPaymentResponse;
import com.mupl.payment_service.dto.response.PaymentProcessResponse;
import com.mupl.payment_service.dto.response.VNPaymentResponse;
import com.mupl.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mupl/payments")
@RequiredArgsConstructor
public class PaymentProcessController {
    private final PaymentService paymentService;
    @PostMapping("/init-payment")
    public ResponseEntity<InitPaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.intPayment(paymentRequest));
    }

    @GetMapping("/vn-pay/callback")
    public ResponseEntity<PaymentProcessResponse> VNPayProcessing(VNPaymentResponse gatewayResponse){
        return ResponseEntity.ok(paymentService.paymentProcess(gatewayResponse));
    }

    @GetMapping("/momo/callback")
    public ResponseEntity<PaymentProcessResponse> momoProcessing(MomoPaymentResponse gatewayResponse){
        return ResponseEntity.ok(paymentService.paymentProcess(gatewayResponse));
    }
}
