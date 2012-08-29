package com.wixpress.streaming.wix;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class WidgetModel {

    private final String instanceToken;
    private final AppInstance appInstance;
    private final boolean publisher;

    public WidgetModel(String instanceToken, AppInstance appInstance, boolean publisher) {
        this.instanceToken = instanceToken;
        this.appInstance = appInstance;
        this.publisher = publisher;
    }

    public String getInstanceToken() {
        return instanceToken;
    }

    public AppInstance getAppInstance() {
        return appInstance;
    }

    public boolean isPublisher() {
        return publisher;
    }
}
