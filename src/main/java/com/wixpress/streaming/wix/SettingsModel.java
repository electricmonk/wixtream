package com.wixpress.streaming.wix;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
public class SettingsModel {

    private final String instanceToken;
    private final Settings settings;

    public SettingsModel(Settings settings, String instanceToken) {
        this.settings = settings;
        this.instanceToken = instanceToken;
    }

    public String getInstanceToken() {
        return instanceToken;
    }

    public Settings getSettings() {
        return settings;
    }
}
