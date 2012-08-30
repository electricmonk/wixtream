package com.wixpress.streaming.chat;

import com.google.common.collect.ImmutableList;
import com.wixpress.streaming.opentok.OpenTokFacade;
import com.wixpress.streaming.opentok.SessionCreationFailedException;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Maps.newConcurrentMap;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class ChatCoordinator {

    private final Map<UUID, TenantSessionHolder> tenants = newConcurrentMap();
    private final OpenTokFacade openTokFacade;

    public ChatCoordinator(OpenTokFacade openTokFacade) {
        this.openTokFacade = openTokFacade;
    }

    public void createSession(UUID instanceId, String clientId) throws ChatCoordinationException {
        TenantSessionHolder tenant = getTenant(instanceId);
        if (tenant.getSessions().containsKey(clientId)) {
            throw new ChatCoordinationException("Client with id " + clientId + " already has a pending session");
        } else {
            tenant.getSessions().put(clientId, new ChatSession(clientId, new DateTime()));
        }
    }

    public ChatSession fulfillSession(UUID instanceId, String clientId) throws ChatCoordinationException {

        TenantSessionHolder tenant = getTenant(instanceId);
        ChatSession session = tenant.getSessions().get(clientId);

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

    public ChatSession getSession(UUID instanceId, String clientId) {
        return getTenant(instanceId).getSessions().get(clientId);
    }

    public List<ChatSession> getSessions(UUID instanceId) {
        return ImmutableList.copyOf(getTenant(instanceId).getSessions().values());
    }

    public void endSession(UUID instanceId, String subscriberClientId) {
        getTenant(instanceId).getSessions().remove(subscriberClientId);
    }

    TenantSessionHolder getTenant(UUID instanceId) {
        if (!tenants.containsKey(instanceId)) {
            tenants.put(instanceId, new TenantSessionHolder());
        }
        return tenants.get(instanceId);
    }
}
