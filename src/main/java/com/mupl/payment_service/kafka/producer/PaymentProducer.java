package com.mupl.payment_service.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mupl.payment_service.entity.PaymentEntity;
import com.mupl.payment_service.entity.UserSubscriptionEntity;
import com.mupl.payment_service.kafka.event.KafkaEvent;
import com.mupl.payment_service.kafka.event.PaymentSuccessPayload;
import com.mupl.payment_service.repository.PaymentRepository;
import com.mupl.payment_service.repository.UserSubscriptionRepository;
import com.mupl.payment_service.util.constant.KafkaEventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentProducer {
    private final KafkaTemplate<String, byte[]> producer;
    private final String PAYMENT_TOPIC = "mupl_payment_topic";
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    @Value("${spring.application.name}")
    private String serviceName;

    public void sendPaymentSuccessEvent(String paymentId) {
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).get();
        UserSubscriptionEntity userSubscriptionEntity = userSubscriptionRepository.findById(paymentEntity.getUsername()).get();
        PaymentSuccessPayload paymentSuccessPayload = PaymentSuccessPayload.builder()
                .username(paymentEntity.getUsername())
                .amount(paymentEntity.getAmount())
                .paymentId(paymentId)
                .endTime(userSubscriptionEntity.getEndTime())
                .build();
        KafkaEvent<PaymentSuccessPayload> event = KafkaEvent.<PaymentSuccessPayload>builder()
                .eventId(UUID.randomUUID().toString())
                .correlationId(UUID.randomUUID().toString())
                .eventTime(LocalDateTime.now())
                .eventType(KafkaEventType.PAYMENT_SUCCESS.getEventName())
                .payload(paymentSuccessPayload)
                .source(serviceName)
                .build();
        try {
            byte[] messageBytes = objectMapper.writeValueAsBytes(event);
            String jsonMessage = new String(messageBytes, StandardCharsets.UTF_8);
            log.info("📤 Sending message: {}", jsonMessage);
            producer.send(PAYMENT_TOPIC, paymentId, messageBytes);
            log.info("Send payment success event with id: {}", paymentId);
        } catch (Exception e) {
            log.error("Error sending sendPaymentSuccessEvent, paymentId {} {}", paymentId, e.getMessage());
        }
    }
}
