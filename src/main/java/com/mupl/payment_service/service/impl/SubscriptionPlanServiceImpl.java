package com.mupl.payment_service.service.impl;

import com.mupl.payment_service.dto.request.SubscriptionPlanRequest;
import com.mupl.payment_service.dto.response.SubscriptionPlanResponse;
import com.mupl.payment_service.entity.SubscriptionPlanEntity;
import com.mupl.payment_service.exception.BadRequestException;
import com.mupl.payment_service.repository.SubscriptionPlanRepository;
import com.mupl.payment_service.service.SubscriptionPlanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final ModelMapper modelMapper;

    @Override
    public SubscriptionPlanResponse createPlan(SubscriptionPlanRequest subscriptionPlanRequest) {
        if (subscriptionPlanRepository.existsBySubscriptionPlanType(subscriptionPlanRequest.getSubscriptionPlanType().trim())) {
            throw new BadRequestException("This subscription plan already exists");
        }
        SubscriptionPlanEntity subscriptionPlanEntity = modelMapper.map(subscriptionPlanRequest, SubscriptionPlanEntity.class);
        subscriptionPlanEntity.setCreatedAt(LocalDateTime.now());
        subscriptionPlanEntity.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(subscriptionPlanRepository.save(subscriptionPlanEntity), SubscriptionPlanResponse.class);
    }

    @Override
    public SubscriptionPlanResponse updatePlan(int planId, SubscriptionPlanRequest subscriptionPlanRequest) {
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new BadRequestException("This subscription plan doesn't exist"));
        if (!subscriptionPlanEntity.getSubscriptionPlanType().equals(subscriptionPlanRequest.getSubscriptionPlanType().trim())) {
            if (subscriptionPlanRepository.existsBySubscriptionPlanType(subscriptionPlanRequest.getSubscriptionPlanType().trim())) {
                throw new BadRequestException("This subscription plan already exists");
            }
        }
        BeanUtils.copyProperties(subscriptionPlanRequest, subscriptionPlanEntity);
        subscriptionPlanEntity.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(subscriptionPlanRepository.save(subscriptionPlanEntity), SubscriptionPlanResponse.class);
    }

    @Override
    public SubscriptionPlanResponse deletePlan(int planId) {
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new BadRequestException("This subscription plan doesn't exist"));
        SubscriptionPlanResponse subscriptionPlanResponse = modelMapper.map(subscriptionPlanEntity, SubscriptionPlanResponse.class);
        subscriptionPlanRepository.delete(subscriptionPlanEntity);
        return subscriptionPlanResponse;
    }

    @Override
    public SubscriptionPlanResponse getPlan(int planId) {
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new BadRequestException("This subscription plan doesn't exist"));
        return modelMapper.map(subscriptionPlanEntity, SubscriptionPlanResponse.class);
    }
}
