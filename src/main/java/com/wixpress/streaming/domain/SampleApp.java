package com.wixpress.streaming.domain;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public abstract class SampleApp {

    // HardCoded defaults for staging environments - can be changed via controller
    protected final static String DEFAULT_APPLICATION_ID = "129a90ff-094d-f193-49a0-2da5d7d2209b";
    protected final static String DEFAULT_SECRET_ID = "39202616-8cfc-4a28-a8d7-4790d13de94e";

    protected final static String INSTANCE = "Instance";
    protected final static String BAGGAGE = "baggage";

    protected String applicationID;
    protected String applicationSecret;

    public SampleApp() {
        this.applicationID = DEFAULT_APPLICATION_ID;
        this.applicationSecret = DEFAULT_SECRET_ID;
    }

    public SampleApp(String applicationID, String applicationSecret) {
        this.applicationID = applicationID;
        this.applicationSecret = applicationSecret;
    }

    public abstract Instance addAppInstance(Instance instance);

    public abstract Instance addAppInstance(WixSignedInstance wixSignedInstance);

    public abstract Instance getAppInstance(UUID instanceId);

    public abstract Instance getAppInstance(WixSignedInstance wixSignedInstance);

    public abstract void update(Instance appInstance);


    public String getApplicationID() {
        return applicationID;
    }

    public String getApplicationSecret() {
        return applicationSecret;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setApplicationSecret(String applicationSecret) {
        this.applicationSecret = applicationSecret;
    }
}
