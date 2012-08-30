package com.wixpress.streaming.paypal;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
public class PaymentModel {

    private final String payKey;

    public PaymentModel(String payKey) {
        this.payKey = payKey;
    }

    public String getPayKey() {
        return payKey;
    }
}
