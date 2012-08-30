package com.wixpress.streaming.paypal;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
public class CompletePaymentRequest {
    private final String token;

    public CompletePaymentRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
