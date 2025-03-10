package com.mupl.payment_service.service;

import com.mupl.payment_service.dto.request.SubscriptionPlanRequest;
import com.mupl.payment_service.dto.response.SubscriptionPlanResponse;

public interface SubscriptionPlanService {
    SubscriptionPlanResponse createPlan(SubscriptionPlanRequest subscriptionPlanRequest);
    SubscriptionPlanResponse updatePlan(int planId, SubscriptionPlanRequest subscriptionPlanRequest);
    SubscriptionPlanResponse deletePlan(int planId);
    SubscriptionPlanResponse getPlan(int planId);
}
