package com.wixpress.streaming.opentok;

import com.opentok.api.OpenTokSDK;
import com.opentok.exception.OpenTokException;
import org.joda.time.DateTime;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class OpenTokFacade {

    private final int apiKey = 17466472;
    private final String apiSecret = "f10140ce6d9604d5f1415040376f2589fd09af14";

    private final OpenTokSDK sdk = new OpenTokSDK(apiKey, apiSecret);

    public OpenTokSession createSession(String clientId) throws SessionCreationFailedException {
        try {
            com.opentok.api.OpenTokSession tokSession = sdk.create_session();
            String publisherToken = sdk.generate_token(tokSession.getSessionId(), "publisher");
            String subscriberToken = sdk.generate_token(tokSession.getSessionId(), "publisher");
            return new OpenTokSession(clientId, tokSession.getSessionId(), new DateTime(), apiKey, publisherToken, subscriberToken);
        } catch (OpenTokException e) {
            throw new SessionCreationFailedException("Failed creating a session for client id" + clientId);
        }
    }

}
