package com.mupl.payment_service.service;

import com.mupl.payment_service.entity.UserSubscriptionEntity;

public interface UserSubscriptionService {
    UserSubscriptionEntity updateUserSubscription(String paymentId);
}
