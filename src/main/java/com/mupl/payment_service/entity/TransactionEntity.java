package com.mupl.payment_service.entity;

import com.mupl.payment_service.util.constant.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mupl_transaction")
@Builder
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Double amount;
    private String bankCode;
    private String bankTranNo;
    private String cardType;
    private String responseMessage;
    private LocalDateTime payDate;
    private String responseCode;
    private String tmnCode;
    private String transactionNo;
    private String transactionStatus;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;
}
