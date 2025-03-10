package com.mupl.payment_service.util.constant;

import lombok.Getter;

@Getter
public enum SubscriptionPlanType {
    A_MONTHLY(1),
    SIX_MONTHLY(6),
    A_YEARLY(12);

    private final int duration;

    SubscriptionPlanType(final int duration) {
        this.duration = duration;
    }
}
