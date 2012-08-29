package com.wixpress.streaming.opentok;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class SessionCreationFailedException extends Exception {

    public SessionCreationFailedException() {
    }

    public SessionCreationFailedException(String message) {
        super(message);
    }

    public SessionCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionCreationFailedException(Throwable cause) {
        super(cause);
    }
}
