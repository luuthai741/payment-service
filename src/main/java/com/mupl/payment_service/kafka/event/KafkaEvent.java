package com.mupl.payment_service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaEvent<T> {
    private String eventId;
    private String eventType;
    private LocalDateTime eventTime;
    private String source;
    private String correlationId;
    private T payload;
}
