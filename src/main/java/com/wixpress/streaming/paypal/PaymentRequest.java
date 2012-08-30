package com.wixpress.streaming.paypal;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
public class PaymentRequest {

    private final Double amount;
    private final String currenyCode;
    private final String receiverEmail;
    private final String returnUrl;

    public PaymentRequest(Double amount, String currenyCode, String receiverEmail, String returnUrl) {
        this.amount = amount;
        this.currenyCode = currenyCode;
        this.receiverEmail = receiverEmail;
        this.returnUrl = returnUrl;
    }

    public Double getAmount() {
        return amount;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getCurrenyCode() {
        return currenyCode;
    }

    public String getReturnUrl() {
        return returnUrl;
    }
}
