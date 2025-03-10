package com.mupl.payment_service.repository;

import com.mupl.payment_service.entity.SubscriptionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Integer> {
    Optional<SubscriptionPlanEntity> findBySubscriptionPlanType(String subscriptionPlanType);
    boolean existsBySubscriptionPlanType(String subscriptionPlanType);
}
