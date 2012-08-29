package com.wixpress.streaming.chat;

import java.util.Map;

import static com.google.common.collect.Maps.newConcurrentMap;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class TenantSessionHolder {

    private final Map<String, ChatSession> sessions = newConcurrentMap();

    public Map<String, ChatSession> getSessions() {
        return sessions;
    }

}
