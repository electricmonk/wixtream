package com.wixpress.streaming.chat;

import com.wixpress.streaming.opentok.OpenTokSession;
import org.joda.time.DateTime;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class ChatSession {

    private final String clientId;
    private final DateTime created;
    private OpenTokSession openTokSession = null;

    public ChatSession(String clientId, DateTime created) {
        this.clientId = clientId;
        this.created = created;
    }

    public OpenTokSession getOpenTokSession() {
        return openTokSession;
    }

    public void setOpenTokSession(OpenTokSession openTokSession) {
        this.openTokSession = openTokSession;
    }

    public String getClientId() {
        return clientId;
    }

    public DateTime getCreated() {
        return created;
    }
}
