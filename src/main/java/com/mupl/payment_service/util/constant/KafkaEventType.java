package com.mupl.payment_service.util.constant;

import lombok.Getter;

@Getter
public enum KafkaEventType {
    SONG_DELETED("SONG_DELETED");
    private final String eventName;

    KafkaEventType(String eventType) {
        this.eventName = eventType;
    }

    public static KafkaEventType fromEventName(String eventName) {
        for (KafkaEventType type : KafkaEventType.values()) {
            if (type.eventName.equals(eventName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + eventName);
    }
}
