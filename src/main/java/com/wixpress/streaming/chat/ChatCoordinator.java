package com.wixpress.streaming.chat;

import com.wixpress.streaming.opentok.OpenTokFacade;
import com.wixpress.streaming.opentok.SessionCreationFailedException;
import org.joda.time.DateTime;

import java.util.Map;

import static com.google.common.collect.Maps.newConcurrentMap;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class ChatCoordinator {

    private final Map<String, ChatSession> sessions = newConcurrentMap();
    private final OpenTokFacade openTokFacade;

    public ChatCoordinator(OpenTokFacade openTokFacade) {
        this.openTokFacade = openTokFacade;
    }

    public void createSession(String clientId) {
        sessions.put(clientId, new ChatSession(clientId, new DateTime()));
    }

    public ChatSession fulfillSession(String clientId) throws ChatCoordinationException {

        ChatSession session = sessions.get(clientId);

        if (session == null) {
            throw new ChatCoordinationException("Client with id " + clientId + " has no pending session");

        } else if (session.getOpenTokSession() != null) {
            throw new ChatCoordinationException("Client with id " + clientId + " already has an active session");

        } else {
            try {
                session.setOpenTokSession(openTokFacade.createSession(session.getClientId()));
                return session;
            } catch (SessionCreationFailedException e) {
                throw new ChatCoordinationException("Failed opening a session for client with id " + clientId, e);
            }
        }
    }

    public ChatSession getSession(String clientId) {
        return sessions.get(clientId);
    }
}
