package com.wixpress.streaming.wix;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class Settings {

    private String displayName;
    private String description;
    private String sessionLength;
    private String paypalMerchantEmail;
    private Double pricePerSessionInUSD = 10.0;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSessionLength() {
        return sessionLength;
    }

    public void setSessionLength(String sessionLength) {
        this.sessionLength = sessionLength;
    }

    public String getPaypalMerchantEmail() {
        return paypalMerchantEmail;
    }

    public void setPaypalMerchantEmail(String paypalMerchantEmail) {
        this.paypalMerchantEmail = paypalMerchantEmail;
    }

    public Double getPricePerSessionInUSD() {
        return pricePerSessionInUSD;
    }

    public void setPricePerSessionInUSD(Double pricePerSessionInUSD) {
        this.pricePerSessionInUSD = pricePerSessionInUSD;
    }
}
