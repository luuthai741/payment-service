package com.mupl.payment_service.service.impl;

import com.mupl.payment_service.entity.PaymentEntity;
import com.mupl.payment_service.entity.SubscriptionPlanEntity;
import com.mupl.payment_service.entity.UserSubscriptionEntity;
import com.mupl.payment_service.exception.BadRequestException;
import com.mupl.payment_service.repository.PaymentRepository;
import com.mupl.payment_service.repository.SubscriptionPlanRepository;
import com.mupl.payment_service.repository.UserSubscriptionRepository;
import com.mupl.payment_service.service.UserSubscriptionService;
import com.mupl.payment_service.util.constant.UserSubscriptionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private final PaymentRepository paymentRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Override
    public UserSubscriptionEntity updateUserSubscription(String paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BadRequestException("Payment not found"));
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanRepository.findById(payment.getSubscriptionPlanId())
                .orElseThrow(() -> new BadRequestException("Subscription plan not found"));
        UserSubscriptionEntity userSubscriptionEntity = userSubscriptionRepository.findById(payment.getUsername())
                .orElse(null);
        if (ObjectUtils.isEmpty(userSubscriptionEntity) || !userSubscriptionEntity.getStatus().equals(UserSubscriptionStatus.ACTIVE)) {
            userSubscriptionEntity = UserSubscriptionEntity.builder()
                    .username(payment.getUsername())
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now().plusMonths(subscriptionPlanEntity.getDurationInMonths()))
                    .status(UserSubscriptionStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            userSubscriptionEntity.setStartTime(LocalDateTime.now());
            userSubscriptionEntity.setEndTime(userSubscriptionEntity.getEndTime().plusMonths(subscriptionPlanEntity.getDurationInMonths()));
            userSubscriptionEntity.setStatus(UserSubscriptionStatus.ACTIVE);
            userSubscriptionEntity.setUpdatedAt(LocalDateTime.now());
        }
        return userSubscriptionRepository.save(userSubscriptionEntity);
    }
}
