package com.wixpress.streaming.domain;

import com.opentok.api.OpenTokSDK;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class TokBoxFacade {

    private final OpenTokSDK openTokSDK;

    public TokBoxFacade(OpenTokSDK openTokSDK) {
        this.openTokSDK = openTokSDK;
    }


}
