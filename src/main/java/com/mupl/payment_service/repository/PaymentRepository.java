package com.mupl.payment_service.repository;

import com.mupl.payment_service.dto.response.PaymentResponse;
import com.mupl.payment_service.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    @Query("SELECT new com.mupl.payment_service.dto.response.PaymentResponse(p.paymentId, p.username, p.amount, 'luuthai555@gmail.com','0888400553', u.endTime) " +
            "FROM PaymentEntity p " +
            "JOIN UserSubscriptionEntity u " +
            "ON u.username = p.username " +
            "WHERE p.paymentId = :paymentId")
    PaymentResponse findByPaymentId(String paymentId);
}
