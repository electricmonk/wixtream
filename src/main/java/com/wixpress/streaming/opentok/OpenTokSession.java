package com.wixpress.streaming.opentok;

import org.joda.time.DateTime;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class OpenTokSession {

    private final String clientId;
    private final String sessionId;
    private final DateTime created;
    private final int apiKey;
    private final String publisherToken;
    private final String subscriberToken;

    public OpenTokSession(String clientId, String sessionId, DateTime created, int apiKey, String publisherToken, String subscriberToken) {
        this.clientId = clientId;
        this.sessionId = sessionId;
        this.created = created;
        this.apiKey = apiKey;
        this.publisherToken = publisherToken;
        this.subscriberToken = subscriberToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public DateTime getCreated() {
        return created;
    }

    public int getApiKey() {
        return apiKey;
    }

    public String getPublisherToken() {
        return publisherToken;
    }

    public String getSubscriberToken() {
        return subscriberToken;
    }
}
