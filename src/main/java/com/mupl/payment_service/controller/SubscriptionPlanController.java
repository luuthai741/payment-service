package com.mupl.payment_service.controller;

import com.mupl.payment_service.dto.request.SubscriptionPlanRequest;
import com.mupl.payment_service.dto.response.SubscriptionPlanResponse;
import com.mupl.payment_service.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mupl/payments")
@RequiredArgsConstructor
public class SubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/plans")
    public ResponseEntity<SubscriptionPlanResponse> createSubscriptionPlan(@RequestBody SubscriptionPlanRequest request) {
        return ResponseEntity.ok(subscriptionPlanService.createPlan(request));
    }

    @PutMapping("/plans/{planId}")
    public ResponseEntity<SubscriptionPlanResponse> updateSubscriptionPlan(@PathVariable Integer planId,
                                                                           @RequestBody SubscriptionPlanRequest request) {
        return ResponseEntity.ok(subscriptionPlanService.updatePlan(planId, request));
    }

    @DeleteMapping("/plans/{planId}")
    public ResponseEntity<SubscriptionPlanResponse> deleteSubscriptionPlan(@PathVariable Integer planId) {
        return ResponseEntity.ok(subscriptionPlanService.deletePlan(planId));
    }

    @GetMapping("/plans/{planId}")
    public ResponseEntity<SubscriptionPlanResponse> getSubscriptionPlan(@PathVariable Integer planId) {
        return ResponseEntity.ok(subscriptionPlanService.getPlan(planId));
    }
}
