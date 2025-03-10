package com.mupl.payment_service.feign;

import com.mupl.payment_service.dto.request.InitMomoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "momo-client", url = "${payment.momo.base-url}")
public interface MomoClient {
    @PostMapping("/create")
    Map<String,String> initMomoPayment(@RequestBody InitMomoRequest initMomoRequest);
}
