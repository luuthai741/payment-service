package com.mupl.payment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mupl.payment_service.util.DateUtils;
import com.mupl.payment_service.util.constant.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VNPaymentResponse extends PaymentCallResponse {
    private Long vnp_Amount;
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_PayDate;
    private String vnp_ResponseCode;
    private String vnp_TmnCode;
    private String vnp_TransactionNo;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_SecureHash;

    public void setVnp_Amount(Long vnp_Amount) {
        super.setAmount(Double.valueOf(vnp_Amount/100));
        this.vnp_Amount = vnp_Amount;
    }

    public void setVnp_BankCode(String vnp_BankCode) {
        super.setBankCode(vnp_BankCode);
        this.vnp_BankCode = vnp_BankCode;
    }

    public void setVnp_BankTranNo(String vnp_BankTranNo) {
        super.setBankTranNo(vnp_BankTranNo);
        this.vnp_BankTranNo = vnp_BankTranNo;
    }

    public void setVnp_CardType(String vnp_CardType) {
        super.setCardType(vnp_CardType);
        this.vnp_CardType = vnp_CardType;
    }

    public void setVnp_OrderInfo(String vnp_OrderInfo) {
        super.setResponseMessage(vnp_OrderInfo);
        this.vnp_OrderInfo = vnp_OrderInfo;
    }

    public void setVnp_PayDate(String vnp_PayDate) {
        super.setPayDate(DateUtils.convertStringToDateTime(vnp_PayDate, DateUtils.yyyyMMddHHmmss));
        this.vnp_PayDate = vnp_PayDate;
    }

    public void setVnp_ResponseCode(String vnp_ResponseCode) {
        super.setResponseCode(vnp_ResponseCode);
        this.vnp_ResponseCode = vnp_ResponseCode;
    }

    public void setVnp_TmnCode(String vnp_TmnCode) {
        super.setTmnCode(vnp_TmnCode);
        this.vnp_TmnCode = vnp_TmnCode;
    }

    public void setVnp_TransactionNo(String vnp_TransactionNo) {
        super.setTransactionNo(vnp_TransactionNo);
        this.vnp_TransactionNo = vnp_TransactionNo;
    }

    public void setVnp_TransactionStatus(String vnp_TransactionStatus) {
        super.setTransactionStatus(vnp_TransactionStatus);
        this.vnp_TransactionStatus = vnp_TransactionStatus;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        super.setPaymentId(vnp_TxnRef);
        super.setPaymentType(PaymentType.VN_PAY);
        this.vnp_TxnRef = vnp_TxnRef;
    }

    @Override
    public String getPaymentId() {
        return super.getPaymentId();
    }

    @Override
    public Double getAmount() {
        return super.getAmount();
    }

    @Override
    public String getBankCode() {
        return super.getBankCode();
    }

    @Override
    public String getBankTranNo() {
        return super.getBankTranNo();
    }

    @Override
    public String getCardType() {
        return super.getCardType();
    }

    @Override
    public String getResponseMessage() {
        return super.getResponseMessage();
    }

    @Override
    public LocalDateTime getPayDate() {
        return super.getPayDate();
    }

    @Override
    public String getResponseCode() {
        return super.getResponseCode();
    }

    @Override
    public String getTmnCode() {
        return super.getTmnCode();
    }

    @Override
    public String getTransactionNo() {
        return super.getTransactionNo();
    }

    @Override
    public String getTransactionStatus() {
        return super.getTransactionStatus();
    }

    @Override
    public void setPaymentId(String paymentId) {
        super.setPaymentId(paymentId);
    }

    @Override
    public void setAmount(Double amount) {
        super.setAmount(amount);
    }

    @Override
    public void setBankCode(String bankCode) {
        super.setBankCode(bankCode);
    }

    @Override
    public void setBankTranNo(String bankTranNo) {
        super.setBankTranNo(bankTranNo);
    }

    @Override
    public void setCardType(String cardType) {
        super.setCardType(cardType);
    }

    @Override
    public void setResponseMessage(String responseMessage) {
        super.setResponseMessage(responseMessage);
    }

    @Override
    public void setPayDate(LocalDateTime payDate) {
        super.setPayDate(payDate);
    }

    @Override
    public void setResponseCode(String responseCode) {
        super.setResponseCode(responseCode);
    }

    @Override
    public void setTmnCode(String tmnCode) {
        super.setTmnCode(tmnCode);
    }

    @Override
    public void setTransactionNo(String transactionNo) {
        super.setTransactionNo(transactionNo);
    }

    @Override
    public void setTransactionStatus(String transactionStatus) {
        super.setTransactionStatus(transactionStatus);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    //    public String getPaymentId(){
//        super.setPaymentId(vnp_TxnRef);
//        return super.paymentId;
//    }

//    public Double getAmount(){
//        super.setAmount(Double.valueOf(vnp_Amount));
//        return super.amount;
//    }

//    public String getBankCode(){
//        super.setBankCode(vnp_BankCode);
//        return super.bankCode;
//    }

//    public String getBankTranNo(){
//        super.setBankTranNo(vnp_BankTranNo);
//        return super.bankTranNo;
//    }

//    public String getCardType(){
//        super.setCardType(vnp_CardType);
//        return super.cardType;
//    }

//    public String getResponseMessage(){
//        super.setResponseMessage(vnp_OrderInfo);
//        return super.responseMessage;
//    }

//    public String getResponseCode(){
//        super.setResponseCode(vnp_ResponseCode);
//        return super.responseCode;
//    }

//    public LocalDateTime getPayDate(){
//        super.setPayDate(DateUtils.convertStringToDateTime(DateUtils.yyyyMMddHHmmss, vnp_PayDate));
//        return super.payDate;
//    }

//    public String getTmnCode(){
//        super.setTmnCode(vnp_TmnCode);
//        return super.tmnCode;
//    }

//    public String getTransactionNo(){
//        super.setTransactionNo(vnp_TransactionNo);
//        return super.transactionNo;
//    }
}
