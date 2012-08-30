package com.wixpress.streaming.paypal;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
public class PaypalException extends Exception {

    public PaypalException() {
    }

    public PaypalException(String message) {
        super(message);
    }

    public PaypalException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaypalException(Throwable cause) {
        super(cause);
    }
}
