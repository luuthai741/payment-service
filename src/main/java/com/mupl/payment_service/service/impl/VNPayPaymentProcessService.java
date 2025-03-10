package com.mupl.payment_service.service.impl;

import com.mupl.payment_service.dto.request.InitPayment;
import com.mupl.payment_service.dto.response.InitPaymentResponse;
import com.mupl.payment_service.dto.response.PaymentCallResponse;
import com.mupl.payment_service.dto.response.PaymentProcessResponse;
import com.mupl.payment_service.entity.PaymentEntity;
import com.mupl.payment_service.entity.TransactionEntity;
import com.mupl.payment_service.exception.BadRequestException;
import com.mupl.payment_service.exception.InitPaymentException;
import com.mupl.payment_service.kafka.producer.PaymentProducer;
import com.mupl.payment_service.repository.PaymentRepository;
import com.mupl.payment_service.repository.TransactionRepository;
import com.mupl.payment_service.service.PaymentProcessService;
import com.mupl.payment_service.service.UserSubscriptionService;
import com.mupl.payment_service.util.DateUtils;
import com.mupl.payment_service.util.constant.PaymentStatus;
import com.mupl.payment_service.util.constant.PaymentType;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VNPayPaymentProcessService implements PaymentProcessService {
    @Value("${payment.vn_pay.payment-url}")
    private String paymentURL;
    @Value("${payment.vn_pay.return-url}")
    private String returnURL;
    @Value("${payment.vn_pay.client}")
    private String clientId;
    @Value("${payment.vn_pay.secret}")
    private String secretKey;
    @Value("${payment.vn_pay.command}")
    private String command;
    @Value("${payment.vn_pay.bankcode}")
    private String bankCode;
    @Value("${payment.vn_pay.version}")
    private String version;

    private final HttpServletRequest request;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;
    private final Map<String, String> responseMap = new HashMap<>();
    private final UserSubscriptionService userSubscriptionService;
    private final PaymentProducer paymentProducer;

    // TESTING PAYMENT ACCOUNT : NCB, 9704198526191432198, NGUYEN VAN A, 07/15, 123456
    @PostConstruct
    void init() {
        responseMap.put("00", "Giao dịch thành công");
        responseMap.put("01", "Giao dịch chưa hoàn tất");
        responseMap.put("02", "Giao dịch bị lỗi");
        responseMap.put("04", "Giao dịch đảo (Khách hàng đã bị trừ tiền tại Ngân hàng nhưng GD chưa thành công ở VNPAY)");
        responseMap.put("05", "VNPAY đang xử lý giao dịch này (GD hoàn tiền)");
        responseMap.put("06", "VNPAY đã gửi yêu cầu hoàn tiền sang Ngân hàng (GD hoàn tiền)");
        responseMap.put("07", "Giao dịch bị nghi ngờ gian lận");
        responseMap.put("09", "GD Hoàn trả bị từ chối");
        responseMap.put("24", "Giao dịch không thành công do: Khách hàng hủy giao dịch");
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    @Override
    public PaymentType getProviderId() {
        return PaymentType.VN_PAY;
    }

    public InitPaymentResponse createPayment(InitPayment initPayment) {
        String paymentUrl = getParamsURL(initPayment);
        return InitPaymentResponse.builder()
                .paymentId(initPayment.getPaymentId())
                .amount(initPayment.getAmount())
                .status(PaymentStatus.PENDING)
                .paymentUrl(paymentUrl)
                .build();
    }

    @Override
    public PaymentProcessResponse paymentProcess(PaymentCallResponse gatewayResponse) {
        PaymentEntity paymentEntity = paymentRepository.findById(gatewayResponse.getPaymentId())
                .orElseThrow(() -> new BadRequestException("Payment not found"));
        if (StringUtils.equalsAny(paymentEntity.getStatus().toString(), PaymentStatus.SUCCESS.toString(), PaymentStatus.FAILED.toString())) {
            throw new BadRequestException("Payment has already been processed");
        }
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(gatewayResponse.getAmount())
                .bankCode(gatewayResponse.getBankCode())
                .bankTranNo(gatewayResponse.getBankTranNo())
                .cardType(gatewayResponse.getCardType())
                .responseMessage(responseMap.get(gatewayResponse.getResponseCode()))
                .responseCode(gatewayResponse.getResponseCode())
                .payDate(gatewayResponse.getPayDate())
                .tmnCode(gatewayResponse.getTmnCode())
                .transactionNo(gatewayResponse.getTransactionNo())
                .transactionStatus(gatewayResponse.getTransactionStatus())
                .payment(paymentEntity)
                .paymentType(gatewayResponse.getPaymentType())
                .build();
        transactionRepository.save(transactionEntity);
        PaymentStatus paymentStatus = getPaymentStatus(gatewayResponse.getTransactionStatus());
        paymentEntity.setStatus(paymentStatus);
        paymentRepository.save(paymentEntity);
        if (paymentStatus.equals(PaymentStatus.SUCCESS)) {
            userSubscriptionService.updateUserSubscription(paymentEntity.getPaymentId());
            paymentProducer.sendPaymentSuccessEvent(paymentEntity.getPaymentId());
        }
        return PaymentProcessResponse.builder()
                .paymentId(paymentEntity.getPaymentId())
                .responseCode(gatewayResponse.getResponseCode())
                .responseMessage(responseMap.get(gatewayResponse.getResponseCode()))
                .status(paymentStatus)
                .build();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.US_ASCII);
    }

    private String hmacSHA512(String key, String data) {
        try {
            if (StringUtils.isAnyBlank(key, data)) {
                throw new InitPaymentException("VN Payment configuration has been error");
            }
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new InitPaymentException("VN Payment configuration has been error");
        }
    }

    private PaymentStatus getPaymentStatus(String transactionStatus) {
        if (StringUtils.equalsAny(transactionStatus, "00")) {
            return PaymentStatus.SUCCESS;
        } else if (StringUtils.equalsAny(transactionStatus, "01")) {
            return PaymentStatus.PENDING;
        } else {
            return PaymentStatus.FAILED;
        }
    }

    private String getParamsURL(InitPayment initPayment) {
        Map<String, String> initVNPaymentParams = new HashMap<>();
        initVNPaymentParams.put("vnp_Version", version);
        initVNPaymentParams.put("vnp_Command", command);
        initVNPaymentParams.put("vnp_TmnCode", clientId);
        initVNPaymentParams.put("vnp_Amount", String.valueOf(initPayment.getAmount().longValue() * 100));
        initVNPaymentParams.put("vnp_CurrCode", "VND");
        initVNPaymentParams.put("vnp_BankCode", "NCB");
        initVNPaymentParams.put("vnp_TxnRef", initPayment.getPaymentId());
        initVNPaymentParams.put("vnp_OrderInfo", "Thanh toan goi:" + initPayment.getItem());
        initVNPaymentParams.put("vnp_OrderType", "other");
        initVNPaymentParams.put("vnp_Locale", "vn");
        initVNPaymentParams.put("vnp_ReturnUrl", returnURL);
        initVNPaymentParams.put("vnp_IpAddr", getIpAddress(request));
        LocalDateTime now = LocalDateTime.now();
        initVNPaymentParams.put("vnp_CreateDate", DateUtils.convertDateTimeToString(now, DateUtils.yyyyMMddHHmmss));
        initVNPaymentParams.put("vnp_ExpireDate", DateUtils.convertDateTimeToString(now.plusMinutes(15), DateUtils.yyyyMMddHHmmss));

        List<String> sortedKeys = new ArrayList<>(initVNPaymentParams.keySet());
        Collections.sort(sortedKeys);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = sortedKeys.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = initVNPaymentParams.get(fieldName);
            if (StringUtils.isNotBlank(fieldValue)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(encode(fieldValue));
                //Build query
                query.append(encode(fieldName));
                query.append('=');
                query.append(encode(fieldValue));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String secureHash = hmacSHA512(secretKey, hashData.toString());
        String queryUrl = query + "&vnp_SecureHash=" + secureHash;
        return paymentURL + "?" + queryUrl;
    }
}
