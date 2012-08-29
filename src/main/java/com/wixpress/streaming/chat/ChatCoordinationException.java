package com.wixpress.streaming.chat;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class ChatCoordinationException extends Exception {
    public ChatCoordinationException() {
    }

    public ChatCoordinationException(String message) {
        super(message);
    }

    public ChatCoordinationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatCoordinationException(Throwable cause) {
        super(cause);
    }

    public ChatCoordinationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}