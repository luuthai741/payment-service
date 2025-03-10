package com.mupl.payment_service.dto.request;

import com.mupl.payment_service.exception.InitPaymentException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitMomoRequest {

    //{
    //  "orderInfo" : "Pay With MoMo",
    //  "amount" : 50000,
    //  "partnerName" : "test MoMo",
    //  "requestType" : "captureWallet",
    //  "redirectUrl" : "https://google.com.vn",
    //  "ipnUrl" : "https://google.com.vn",
    //  "storeId" : "test store ID",
    //  "extraData" : "",
    //  "autoCapture" : true,
    //  "signature" : "5e560df3fbd66e90901ea0d49a6ea331590ac64b93ddd0a70ff29b1236bb2432",
    //  "partnerCode" : "MOMOLRJZ20181206",
    //  "requestId" : "1741268559682",
    //  "orderId" : "1741268559682",
    //  "lang" : "en",
    //  "startTime" : 1741268571509
    //}
    private String orderInfo;
    private Long amount;
    private String partnerName;
    private String requestType;
    private String redirectUrl;
    private String ipnUrl;
    private String storeId;
    private String extraData;
    private Boolean autoCapture;
    private String signature;
    private String partnerCode;
    private String requestId;
    private String orderId;
    private String lang;
    private String startTime;

    public void setSignature(String accessKey, String secretKey) {
        StringBuilder rawParams = new StringBuilder();
        rawParams
                .append("accessKey").append("=").append(accessKey).append("&")
                .append("amount").append("=").append(amount).append("&")
                .append("extraData").append("=").append(extraData).append("&")
                .append("ipnUrl").append("=").append(ipnUrl).append("&")
                .append("orderId").append("=").append(orderId).append("&")
                .append("orderInfo").append("=").append(orderInfo).append("&")
                .append("partnerCode").append("=").append(partnerCode).append("&")
                .append("redirectUrl").append("=").append(redirectUrl).append("&")
                .append("requestId").append("=").append(requestId).append("&")
                .append("requestType").append("=").append(requestType);
        try {
            signature = signHmacSHA256(rawParams.toString(), secretKey);
        } catch (Exception e) {
            throw new InitPaymentException("Momo configuration has been error");
        }
    }

    private String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return sb.toString();
    }

    public String signHmacSHA256(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return toHexString(rawHmac);
    }
}
