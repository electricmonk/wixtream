package com.wixpress.streaming.paypal;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
public class PaymentModel {

    private final String token;

    public PaymentModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
