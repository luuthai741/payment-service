package com.mupl.payment_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mupl.payment_service.exception.BadRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper;

    public CustomErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage = "Unknown error"; // Giá trị mặc định nếu không parse được

        try {
            if (response.body() != null) {
                errorMessage = objectMapper.readTree(response.body().asInputStream())
                        .path("message")
                        .asText();
            }
        } catch (IOException e) {
            log.error("Error decoding response body", e);
        }

        switch (response.status()) {
            case 400:
                return new BadRequestException(errorMessage);
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
