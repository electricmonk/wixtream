package com.wixpress.streaming.wix;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public class AppInstance {

    private UUID instanceId;
    private DateTime signDate;
    private DateTime lastAccessedDate;
    private UUID uid;
    private String permissions = "";

    private int width;
    private int height;

    private Settings settings = new Settings();

    public AppInstance(){}

    public AppInstance(WixSignedInstance wixSignedInstance) {
        this.instanceId = wixSignedInstance.getInstanceId();
        this.signDate = wixSignedInstance.getSignDate();
        this.lastAccessedDate = wixSignedInstance.getSignDate();
        this.uid = wixSignedInstance.getUid();
        this.permissions = wixSignedInstance.getPermissions();
    }

    public DateTime getLastAccessedDate() {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(DateTime lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
    }

    public UUID getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }

    public DateTime getSignDate() {
        return signDate;
    }

    public void setSignDate(DateTime signDate) {
        this.signDate = signDate;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
